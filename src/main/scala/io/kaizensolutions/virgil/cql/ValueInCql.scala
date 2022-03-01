package io.kaizensolutions.virgil.cql

import io.kaizensolutions.virgil.codecs.CqlEncoder

/**
 * Represents a Scala value present in a CQL interpolated string which
 * ultimately needs to be sent to Cassandra along with a query ValueInCql also
 * has the sufficient capability to write the data into the underlying Datastax
 * statement
 */
private[virgil] trait ValueInCql {
  type ScalaType
  def value: ScalaType
  def writer: CqlEncoder[ScalaType]

  override def toString: String =
    value.toString
}
object ValueInCql {
  // Type Refinement
  type WithScalaType[Sc] = ValueInCql { type ScalaType = Sc }

  /**
   * This implicit conversion automatically captures the value and evidence of
   * the type's Writer in a cql interpolated string that is necessary to write
   * data into the Datastax statement
   */
  implicit def scalaTypeToValueInCqlInterpolator[Scala](
    in: Scala
  )(implicit evidence: CqlEncoder[Scala]): ValueInCql.WithScalaType[Scala] =
    new ValueInCql {
      type ScalaType = Scala
      val value: Scala              = in
      val writer: CqlEncoder[Scala] = evidence
    }
}
