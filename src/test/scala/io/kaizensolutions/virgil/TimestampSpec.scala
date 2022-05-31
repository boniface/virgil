package io.kaizensolutions.virgil

import io.kaizensolutions.virgil.TimestampSpecDatatypes.ObjectWithTimestamp._
import io.kaizensolutions.virgil.TimestampSpecDatatypes._
import io.kaizensolutions.virgil.dsl._
import zio.Random
import zio.test.TestAspect.{samples, sequential}
import zio.test.{assertTrue, check, suite, test, Gen}

import java.time.LocalDateTime

object TimestampSpec {
  def localDateTimeSpec =
    suite("Timestamp Operators Specification") {
      test("isNull") {
        check(insertTimeStampSpecGen) { timestampObject =>
          val update =
            UpdateBuilder(table)
              .set(Timestamp := timestampObject.timestamp)
              .where(Id === timestampObject.id)
              .ifCondition(Timestamp.isNull)
              .build
              .execute
              .runDrain

          val find = ObjectWithTimestamp.find(timestampObject.id).execute.runHead.some

          truncate.execute.runDrain *>
            update *>
            find.map(actual => assertTrue(actual == timestampObject))
        }
      }
    } @@ sequential @@ samples(4)

  def insertTimeStampSpecGen: Gen[Random, ObjectWithTimestamp] =
    for {
      id        <- Gen.int(1, 1000)
      timestamp <- Gen.localDateTime.map(_ => LocalDateTime.now())
    } yield ObjectWithTimestamp(id, timestamp)

}
