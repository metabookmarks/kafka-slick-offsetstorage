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

import org.apache.kafka.common.TopicPartition
import org.apache.zookeeper.{ WatchedEvent, Watcher, ZooKeeper }

import scala.concurrent.{ ExecutionContext, Future }

abstract class OffsetStore(zookeeperQuorum: String) {

  /**
    * nops zookeeper node watcher.
    */
  private val dummyWatcher = new Watcher {
    override def process(event: WatchedEvent): Unit = ()
  }

  /**
    * Zookeeper request.
    * @param topicName
    * @return
    */
  protected def partitionCount(topicName: String): Future[Int] =
    TWR.probably(new ZooKeeper(zookeeperQuorum, 10000, dummyWatcher)) { zk =>
      val zkNodeName = s"/brokers/topics/$topicName/partitions"
      zk.getChildren(zkNodeName, false).size
    }

  protected def getFromStorage(topic: String,
                               consumer: String): Future[Seq[(TopicPartition, Long)]]

  protected def newPartitions(topic: String,
                              consumer: String,
                              value: Range): Future[Seq[(TopicPartition, Long)]]

  /**
    * Return the partition and offsets for a given topic and consumer.
    *
    * @param topic
    * @param consumer
    * @return
    */
  def getPartitionOffsets(topic: String, consumer: String)(
      implicit ec: ExecutionContext
  ): Future[Seq[(TopicPartition, Long)]] =
    for {
      count <- partitionCount(topic)
      os    <- getFromStorage(topic, consumer)
      np    <- newPartitions(topic, consumer, os.size until count)
    } yield os ++ np

}
