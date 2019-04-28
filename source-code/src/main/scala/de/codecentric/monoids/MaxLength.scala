package de.codecentric.monoids

import cats.kernel.Monoid

case class MaxLength(value: String) extends AnyVal

object MaxLength {
  implicit val maxLengthMonoid: Monoid[MaxLength] = new Monoid[MaxLength] {
    override def empty: MaxLength = MaxLength("")

    override def combine(x: MaxLength, y: MaxLength): MaxLength =
      if (x.value.length >= y.value.length) x else y
  }
}
