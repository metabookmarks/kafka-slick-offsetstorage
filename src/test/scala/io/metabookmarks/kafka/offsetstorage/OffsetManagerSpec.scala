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

import org.scalatest.WordSpec
import slick.jdbc.PostgresProfile
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt

class OffsetManagerSpec extends WordSpec {

  "OffsetManager" ignore {

    val db: PostgresProfile.backend.Database = Database.forConfig("kafkademo")

    val kaflaConnect = sys.env.get("KAFKA_IT_CONNECT").getOrElse("localhost:9092")

    "init RDBMS table from zookeeper" in {
      val fut = OffsetManager(db, kaflaConnect)
        .getPartitionOffsets("test", "test")

      fut.foreach { o =>
        println(s"$o")

      }

      Await.result(fut, 10 seconds)
    }
  }

}
