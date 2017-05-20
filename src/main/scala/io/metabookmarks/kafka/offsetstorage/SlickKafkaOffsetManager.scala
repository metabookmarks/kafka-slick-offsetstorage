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
import slick.jdbc.{ JdbcBackend, JdbcProfile }

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Slick storage implementation.
  */
class SlickKafkaOffsetManager(db: JdbcBackend.Database,
                              dbProfile: JdbcProfile,
                              zookeeperQuorum: String)
    extends OffsetStore(zookeeperQuorum) {

  import dbProfile.api._

  private val offsets = TableQuery[Offset]

  protected def newPartitions(topic: String,
                              consumer: String,
                              newRange: Range): Future[Seq[(TopicPartition, Long)]] =
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

  protected def getFromStorage(topic: String,
                               consumer: String): Future[Seq[(TopicPartition, Long)]] =
    db.run((for {
        o <- offsets if o.topic === topic && o.consumer === consumer
      } yield o).result)
      .map(_.map(o => new TopicPartition(o.topic, o.partition) -> o.offset))

  override def update(consumer: String, topicOffsets: Seq[(TopicPartition, Long)]): Future[Long] = {
    val r = topicOffsets.map {
      case (tp, o) => offsets.insertOrUpdate(OffsetRow(tp.topic, tp.partition, consumer, o))
    }
    db.run(DBIO.sequence(r)).map(cs => cs.sum)
  }
}
