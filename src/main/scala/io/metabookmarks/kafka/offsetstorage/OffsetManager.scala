/*
 * Copyright 2017 Olivier NOUGUIER
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.metabookmarks.kafka.offsetstorage

import io.metabookmarks.kafka.offsetstorage.Tables._
import org.apache.kafka.common.TopicPartition
import org.apache.zookeeper.{ WatchedEvent, Watcher, ZooKeeper }
import slick.jdbc.PostgresProfile
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  *
  * @param db
  * @param zookeeperQuorum
  */
class OffsetManager(db: PostgresProfile.backend.Database, zookeeperQuorum: String) {

  /**
    * nops zookeeper node watcher.
    */
  private val dummyWatcher = new Watcher {
    override def process(event: WatchedEvent): Unit = ()
  }

  private val offsets = TableQuery[Offset]

  /**
    * Zookeeper request.
    * @param topicName
    * @return
    */
  private def partitionCount(topicName: String): Future[Int] =
    TWR.probably(new ZooKeeper(zookeeperQuorum, 10000, dummyWatcher)) { zk =>
      val zkNodeName = s"/brokers/topics/$topicName/partitions"
      zk.getChildren(zkNodeName, false).size
    }

  /**
    * Return the partition and offsets for a given topic and consumer.
    *
    * @param topic
    * @param consumer
    * @return
    */
  def getPartitionOffsets(topic: String, consumer: String): Future[Seq[(TopicPartition, Long)]] = {

    def newPartitions(newRange: Range): Future[Seq[(TopicPartition, Long)]] =
      db.run(
          DBIO
            .sequence(newRange.map { i =>
              offsets += OffsetRow(topic, i, consumer, 0L)
            })
            .transactionally
        )
        .map { _ =>
          newRange.map { i =>
            new TopicPartition(topic, i) -> 0L
          }
        }

    for {
      count <- partitionCount(topic)
      os    <- getFromDB(topic, consumer)
      np    <- newPartitions(os.size until count)
    } yield os ++ np

  }

  private def getFromDB(topic: String, consumer: String) =
    db.run((for {
        o <- offsets if o.topic === topic && o.consumer === consumer
      } yield o).result)
      .map(_.map(o => new TopicPartition(o.topic, o.partition) -> o.offset))

}

object OffsetManager {
  def apply(db: PostgresProfile.backend.Database, zookeeperQuorom: String) =
    new OffsetManager(db, zookeeperQuorom)
}
