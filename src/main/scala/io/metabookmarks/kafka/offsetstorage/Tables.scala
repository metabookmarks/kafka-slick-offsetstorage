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
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.jdbc.PostgresProfile
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{ GetResult => GR }

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Offset.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Offset
    *  @param topic Database column topic SqlType(text)
    *  @param partition Database column partition SqlType(int4)
    *  @param consumer Database column consumer SqlType(text)
    *  @param offset Database column offset SqlType(int8) */
  final case class OffsetRow(topic: String, partition: Int, consumer: String, offset: Long)

  /** GetResult implicit for fetching OffsetRow objects using plain SQL queries */
  implicit def GetResultOffsetRow(implicit e0: GR[String],
                                  e1: GR[Int],
                                  e2: GR[Long]): GR[OffsetRow] = GR { prs =>
    import prs._
    OffsetRow.tupled((<<[String], <<[Int], <<[String], <<[Long]))
  }

  /** Table description of table offset. Objects of this class serve as prototypes for rows in queries. */
  class Offset(_tableTag: Tag) extends profile.api.Table[OffsetRow](_tableTag, "offset") {
    def * = (topic, partition, consumer, offset) <> (OffsetRow.tupled, OffsetRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? =
      (Rep.Some(topic), Rep.Some(partition), Rep.Some(consumer), Rep.Some(offset)).shaped.<>({ r =>
        import r._; _1.map(_ => OffsetRow.tupled((_1.get, _2.get, _3.get, _4.get)))
      }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column topic SqlType(text) */
    val topic: Rep[String] = column[String]("topic")

    /** Database column partition SqlType(int4) */
    val partition: Rep[Int] = column[Int]("partition")

    /** Database column consumer SqlType(text) */
    val consumer: Rep[String] = column[String]("consumer")

    /** Database column offset SqlType(int8) */
    val offset: Rep[Long] = column[Long]("offset")

    /** Primary key of Offset (database name offset_pkey) */
    val pk = primaryKey("offset_pkey", (topic, partition))
  }

  /** Collection-like TableQuery object for table Offset */
  lazy val Offset = new TableQuery(tag => new Offset(tag))
}
