package de.codecentric.applicative.stream.akka

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.{FlatSpec, Matchers}

class WcTest extends FlatSpec with Matchers with TypeCheckedTripleEquals {
  behavior of classOf[Wc].getSimpleName

  private[this] implicit val actorSystem: ActorSystem = ActorSystem()
  private[this] implicit val actorMaterializer: ActorMaterializer =
    ActorMaterializer()

  it should "count a single char as 1 word and 1 char" in {
    def input() = "\u0000".toIterator

    val result = new Wc {
      override def materializer: ActorMaterializer = actorMaterializer
    }.run(input())

    result should ===((0, 1, 1))
  }
}
