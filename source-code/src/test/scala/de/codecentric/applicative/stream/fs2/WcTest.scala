package de.codecentric.applicative.stream.fs2

import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.{FlatSpec, Matchers}

class WcTest extends FlatSpec with Matchers with TypeCheckedTripleEquals {
  behavior of classOf[Wc].getSimpleName

  it should "not count single chars as a word" in {
    def input() = "\u0000".toIterator

    val result = Wc.run(input())

    result should ===((0, 1, 1))
  }
}
