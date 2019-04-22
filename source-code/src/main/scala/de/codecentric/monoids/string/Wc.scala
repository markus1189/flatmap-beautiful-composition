package de.codecentric.monoids.string

import cats.instances.int._
import cats.instances.tuple._
import cats.kernel.Monoid
import mouse.boolean._

trait Wc {
  def run(input: Iterator[Char]): (Int, Int, Int) =
    runMonoid(step)(Wc.wordsAndSkipped(input))

  def runMonoid[M: Monoid](f: (Int, String) => M)(
      input: Iterator[(Int, String)]): M = Monoid[M].combineAll(input.map(f.tupled))

  private[this] def step(skip: Int, w: String): (Int, Int, Int) = {
    println(s"input is : '$w'")
      (countLines(w), countWords(w), countChars(skip, w))
    }

  def countLines(w: String): Int = w.contains("\n") ?? 1

  def countWords(w: String): Int = 0 // shrug

  def countChars(skip: Int, w: String): Int = skip + w.length
}

object Wc extends Wc {
  def wordsAndSkipped(input: Iterator[Char]): Iterator[(Int, String)] =
    new Iterator[(Int, String)] {
      override def hasNext: Boolean = input.hasNext

      override def next(): (Int, String) = {
        var skipped: Int = 0
        var current = input.next()
//        if (input.hasNext) skipped += 1

        while (current.isSpaceChar) {
          skipped += 1
          current = input.next()
        }

        (skipped, current + input.takeWhile(!_.isWhitespace).mkString)
      }
    }
}
