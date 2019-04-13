package de.codecentric.imperative

import java.nio.file.Paths

import de.codecentric.shell
import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.{FlatSpec, Matchers}

import scala.io.Source

class WcTest extends FlatSpec with TypeCheckedTripleEquals with Matchers {
  behavior of classOf[Wc].getSimpleName

  it should "return the same result as the wc unix command on the scala preface file" in {
    val wc = new Wc {}
    val text = Source.fromResource("scala-preface.txt")

    val result = wc.run(text.iter)

    val file = Paths.get(
      Thread.currentThread.getContextClassLoader
        .getResource("scala-preface.txt")
        .toURI)

    result should ===(shell.Wc.run(file))
  }
}
