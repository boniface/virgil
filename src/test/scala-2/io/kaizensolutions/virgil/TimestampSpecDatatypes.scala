package io.kaizensolutions.virgil

import io.kaizensolutions.virgil.cql._
import io.kaizensolutions.virgil.dsl._

import java.time.LocalDateTime

object TimestampSpecDatatypes {
  final case class ObjectWithTimestamp(
    id: Int,
    timestamp: LocalDateTime
  )

  object ObjectWithTimestamp {
    val Id   = "id"
    val Timestamp  = "timestamp"
    val table: String = "timestampspec"
    val truncate: CQL[MutationResult] = s"TRUNCATE TABLE $table".asCql.mutation

    def insert(in: ObjectWithTimestamp): CQL[MutationResult] =
      InsertBuilder(table)
        .value(Id, in.id)
        .value(Timestamp, in.timestamp)
        .build

    def find(id: Int): CQL[ObjectWithTimestamp] =
      SelectBuilder
        .from(table)
        .columns(Id, Timestamp)
        .where(Id === id)
        .build[ObjectWithTimestamp]
  }
}
