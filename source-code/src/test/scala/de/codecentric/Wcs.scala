package de.codecentric

import java.nio.file.Paths

import scala.io.Source

object Wcs extends App {
  def textIter = Source.fromResource("scala-preface.txt").iter

  println(s"${shell.Wc.run(Paths.get("src/test/resources/scala-preface.txt"))} - Shell")
  println(s"${imperative.Wc.run(textIter)} - Imperative")
  println(s"${monoids.char.Wc.run(textIter)} - Monoid (Char)")
  println(s"${monoids.string.Wc.run(textIter)} - Monoid (String)")

}
