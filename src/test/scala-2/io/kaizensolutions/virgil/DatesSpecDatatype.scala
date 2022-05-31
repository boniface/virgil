package io.kaizensolutions.virgil

import io.kaizensolutions.virgil.cql._
import io.kaizensolutions.virgil.dsl._

import java.time.LocalDateTime

object DatesSpecDatatype {
  final case class ClassWithLocalTimeDate(
    id: Int,
    name: String,
    date: LocalDateTime
  )

  object ClassWithLocalTimeDate {
    val Id   = "id"
    val Name = "name"
    val Date  = "date"

    val table: String = "local_time_date"

    val truncate: CQL[MutationResult] = s"TRUNCATE TABLE $table".asCql.mutation

    def insert(in: ClassWithLocalTimeDate): CQL[MutationResult] =
      InsertBuilder(table)
        .value(Id, in.id)
        .value(Name, in.name)
        .value(Date, in.date)
        .build

    def find(id: Int): CQL[ClassWithLocalTimeDate] =
      SelectBuilder
        .from(table)
        .columns(Id, Name, Date)
        .where(Id === id)
        .build[ClassWithLocalTimeDate]
  }

}
