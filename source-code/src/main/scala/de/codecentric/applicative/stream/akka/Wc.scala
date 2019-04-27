package de.codecentric.applicative.stream.akka

import mouse.boolean._
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Keep, Sink, Source}
import cats.data.{Const, Nested, Tuple2K}
import cats.effect.IO
import cats.effect.concurrent.Ref
import de.codecentric.applicative.Utils
import cats.instances.int._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

trait Wc {
  def materializer: ActorMaterializer

  def run(input: Iterator[Char]): (Int, Int, Int) = {
    val src = Source.fromIterator(() => input)

    val r = src
      .toMat(sink(ExecutionContext.global))(Keep.right)
      .run()(materializer)
    Await.result(r, Duration.Inf)
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

  def sink(
      implicit ec: ExecutionContext): Sink[Char, Future[(Int, Int, Int)]] = {

    val ref = Ref.of[IO, Boolean](false).unsafeRunSync

    Utils
      .sinkTraverse_ { c: Char =>
        Tuple2K(Tuple2K(countChars[Unit](c), countLines[Unit](c)),
                countWords[Unit](ref)(c))
      }
      .mapMaterializedValue(_.map {
        case Tuple2K(Tuple2K(chars, lines), words) =>
          (lines.getConst, words.value.unsafeRunSync().getConst, chars.getConst)
      })
  }
}
