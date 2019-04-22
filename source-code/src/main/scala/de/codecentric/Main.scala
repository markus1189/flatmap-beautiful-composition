package de.codecentric

import java.time.LocalDateTime

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Keep, Sink, Source}
import cats.data._
import cats.instances.int._
import cats.syntax.applicative._
import cats.syntax.apply._
import cats.{Applicative, Eval}

import scala.concurrent.Future

object Hello extends App {

  def measure(input: Int) = {
    val m1: Int = 1
    val m2 = Set(input)

    val p1 = Const.of[Unit](m1 -> m2)
    val p2 = Nested(state(input) *> countEven)
    val p3 = Nested(state(input) *> countOdd)

    Tuple2K(p3, p2)
  }

  sealed trait EvenOdd
  case object Even extends EvenOdd
  case object Odd extends EvenOdd

  def countEven: IndexedStateT[Eval, EvenOdd, EvenOdd, Const[Int, Unit]] = State.inspect {
    case Even => Const.of[Unit](1)
    case Odd => Const.of[Unit](0)
  }

  def countOdd: IndexedStateT[Eval, EvenOdd, EvenOdd, Const[Int, Unit]] = State.inspect {
    case Odd => Const.of[Unit](1)
    case Even => Const.of[Unit](0)
  }

  def state(input: Int): IndexedStateT[Eval, EvenOdd, EvenOdd, Unit] = {
    if (input % 2 == 0) {
      State.set[EvenOdd](Even)
    } else {
      State.set[EvenOdd](Odd)
    }
  }

  def sinkTraverse_[F[_]:Applicative, A](f: A => F[Unit]): Sink[A, Future[F[Unit]]] = Sink.fold[F[Unit],A](().pure[F])((u, a) => u <* f(a))

  implicit val system = ActorSystem()
  implicit val mat = ActorMaterializer()

  import system.dispatcher

  Source.unfold(0)(s => Some(s+1).filter(_ < 1000).map(x => (x,x))).take(100000).toMat(sinkTraverse_(measure))(Keep.right).run().flatMap { result =>

    println(s"[${LocalDateTime.now}] stream is done")
    val r1 = result.first.value.run(Even).value._2
    val r2 = result.second.value.run(Even).value._2
    println(s"[${LocalDateTime.now}] The result is: ${r1 -> r2}")

    mat.shutdown()
    system.terminate()
  }

}