package de.codecentric.applicative.stream.fs2

import cats.data.{Const, Nested, Tuple2K}
import cats.effect.concurrent.Ref
import cats.effect.{ContextShift, IO}
import cats.instances.int._
import de.codecentric.applicative.Utils._
import mouse.boolean._

import scala.concurrent.ExecutionContext

trait Wc {

  def run(input: Iterator[Char]): (Int, Int, Int) = {
    implicit val ioContextShift: ContextShift[IO] =
      IO.contextShift(ExecutionContext.global)

    val x = fs2.Stream
      .fromIterator[IO, Char](input)
      .split(_ == '\n')
      .mapAsync(10) { chunk =>
        val ref = Ref.of[IO, Boolean](false).unsafeRunSync
        IO {
          chunk.traverse_(
            c =>
              Tuple2K(Tuple2K(countChars[Unit](c), countLines[Unit](c)),
                      countWords[Unit](ref)(c)))
        }
      }


//    x.tra
//
//    x.traverse_(x => identity(x))
//
//    val y = x.traverse_(identity)
    //    val src = Source.fromIterator(() => input)
//
//    val r = src
//      .toMat(sink(ExecutionContext.global))(Keep.right)
//      .run()(materializer)
//    Await.result(r, Duration.Inf)

    ???
  }

  private[this] def countChars[A](c: Char): Const[Int, A] = Const.of(1)

  private[this] def countLines[A](c: Char): Const[Int, A] =
    (c == '\n') ?? Const.of[A](1)

  private[this] def countWords[A](ref: Ref[IO, Boolean])(c: Char) = Nested {
    for {
      before <- ref.get
      after = !c.isWhitespace
      _ <- ref.set(after)
    } yield (!before && after) ?? Const.of[A](1)
  }

}
