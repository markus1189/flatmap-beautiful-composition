package de.codecentric.monoids.char

import cats.instances.int._
import cats.instances.tuple._
import cats.kernel.Monoid
import cats.syntax.monoid._

trait Wc {
  //snippet:wc-monoid-char
  def run(input: Iterator[Char]): (Int, Int, Int) = runMonoid(step)(input)

  def runMonoid[M: Monoid](f: Char => M)(input: Iterator[Char]): M =
    input.map(f).foldLeft(Monoid.empty[M])(_ |+| _)

  def step(c: Char): (Int, Int, Int) =
    (countLines(c), countWords(c), countChars(c))

  def countLines(c: Char): Int = if (c == '\n') 1 else 0

  def countWords(c: Char): Int = 0 // how?

  def countChars(c: Char): Int = 1
//end
}

object Wc extends Wc
