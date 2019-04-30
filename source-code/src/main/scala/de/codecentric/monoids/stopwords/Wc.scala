package de.codecentric.monoids
package stopwords

import cats.instances.int._
import cats.instances.option._
import cats.kernel.Monoid
import mouse.boolean._

trait Wc {
  def run(isStopword: String => Boolean)(input: Iterator[Char]): Option[Int] =
    runMonoid(countWords(isStopword))(wordsAndSkipped(input).map(_._2))

  def runMonoid[M: Monoid](f: String => M)(input: Iterator[String]): M =
    Monoid[M].combineAll(input.map(f))

  def countWords(isStopword: String => Boolean)(w: String): Option[Int] =
    isStopword(w).option(1)
}
