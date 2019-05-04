package de.codecentric

import scala.io.Source

object ScalaPreface extends App {
  private[this] def textIter(): Iterator[Char] =
    Source.fromResource("scala-preface.txt").iter

  println(s"${shell.Wc.run(textIter())} - Shell")
  println(s"${imperative.Wc.run(textIter())} - Imperative")
  println(s"${monoids.char.Wc.run(textIter())} - Monoid (Char)")
  println(s"${monoids.string.Wc.run(textIter())} - Monoid (String)")
  println(s"${applicative.Wc.run(textIter())} - Applicative (State)")
  println(s"${applicative.io.Wc.run(textIter())} - Applicative (IO)")
  println(s"${applicative.stream.fs2.Wc.run(textIter())} - Applicative (FS2)")
}

object MobyDick extends App {
  private[this] def textIter(): Iterator[Char] =
    Source.fromResource("moby-dick.txt").iter

  println(s"${shell.Wc.run(textIter())} - Shell")
  println(s"${imperative.Wc.run(textIter())} - Imperative")
  println(s"${monoids.char.Wc.run(textIter())} - Monoid (Char)")
  println(s"${monoids.string.Wc.run(textIter())} - Monoid (String)")
  println(s"${applicative.Wc.run(textIter())} - Applicative (State)")
  println(s"${applicative.io.Wc.run(textIter())} - Applicative (IO)")
  println(s"${applicative.stream.fs2.Wc.run(textIter())} - Applicative (FS2)")
}
