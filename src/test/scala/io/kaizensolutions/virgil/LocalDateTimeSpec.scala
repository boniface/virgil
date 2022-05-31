package io.kaizensolutions.virgil


import io.kaizensolutions.virgil.DatesSpecDatatype._
import io.kaizensolutions.virgil.DatesSpecDatatype.ClassWithLocalTimeDate._
import io.kaizensolutions.virgil.dsl._
import zio.Random
import zio.test.TestAspect.{samples, sequential}
import zio.test.{Gen, assertTrue, check, suite, test}

import java.time.LocalDateTime

object LocalDateTimeSpec {
  def localDateTimeSpec =
    suite("Local Date Operators Specification") {
      test("isNull") {
        check(localTime_Date_GenSpec) { localDate =>
          val update =
            UpdateBuilder(table)
              .set(Name := localDate.name)
              .set(Date := localDate.date)
              .where(Id === localDate.id)
              .ifCondition(Name.isNull)
              .build
              .execute
              .runDrain

          val find = ClassWithLocalTimeDate.find(localDate.id).execute.runHead.some

          truncate.execute.runDrain *>
            update *>
            find.map(actual => assertTrue(actual == localDate))
        }
      } + test("isNotNull") {
        check(localTime_Date_GenSpec) { localDate =>
          val insert  = ClassWithLocalTimeDate.insert(localDate).execute.runDrain
          val newName = localDate.name + " " + localDate.name
          val update =
            UpdateBuilder(ClassWithLocalTimeDate.table)
              .set(Name := newName)
              .where(Id === localDate.id)
              .ifCondition(Name.isNotNull)
              .build
              .execute
              .runDrain

          val find = ClassWithLocalTimeDate.find(localDate.id).execute.runHead.some

          truncate.execute.runDrain *>
            insert *>
            update *>
            find.map(actual => assertTrue(actual == localDate.copy(name = newName)))
        }
      }
    } @@ sequential @@ samples(4)

  def localTime_Date_GenSpec: Gen[Random, ClassWithLocalTimeDate] =
    for {
      id   <- Gen.int(1, 1000)
      name <- Gen.stringBounded(2, 4)(Gen.alphaChar)
      date  <- Gen.localDateTime.map(_=> LocalDateTime.now())
    } yield ClassWithLocalTimeDate(id, name, date)

}
