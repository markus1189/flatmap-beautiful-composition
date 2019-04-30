package de.codecentric.monoids
package string

import cats.instances.int._
import cats.instances.tuple._
import cats.kernel.Monoid
import mouse.boolean._

trait Wc {
  //snippet:wc-monoid-string
  def run(input: Iterator[Char]): (Int, Int, Int) =
    // wordsAndSkipped: Iterator[Char] => Iterator[(Int, String)]
    runMonoid(step)(wordsAndSkipped(input))

  def runMonoid[M: Monoid](f: (Int, String) => M)(
      input: Iterator[(Int, String)]): M =
    Monoid[M].combineAll(input.map(f.tupled))

  def step(skip: Int, w: String): (Int, Int, Int) =
    (countLines(w), countWords(w), countChars(skip, w))

  def countLines(w: String): Int = 0 // damn...

  def countWords(w: String): Int = 1

  def countChars(skip: Int, w: String): Int = skip + w.length
//end
}