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

import scala.concurrent.{ Future, Promise }
import scala.util.{ Failure, Success, Try }
import scala.concurrent.ExecutionContext.Implicits.global
object TWR {

  def twr[A <: { def close() }, B](zk: A)(f: (A) => B) = {
    var a: Option[B] = None
    try {
      a = Some(f(zk))
    } finally {
      zk.close()
    }
    a
  }

  def cleanly[A <: { def close() }, B](a: A)(doJob: A => B): Try[B] =
    try {
      Success(doJob(a))
    } catch {
      case e: Exception => Failure(e)
    } finally {
      a.close()
    }

  def probably[A <: { def close() }, B](a: A)(doJob: A => B): Future[B] = {
    val promise = Promise[B]
    Future {
      cleanly(a)(doJob) match {
        case Success(b) => promise.success(b)
        case Failure(e) => promise.failure(e)
      }
    }
    promise.future
  }
}
