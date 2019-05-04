package de.codecentric.applicative.stream.fs2

import cats.data.{Const, Nested, Tuple2K}
import cats.effect.concurrent.Ref
import cats.effect.{ContextShift, IO}
import cats.instances.int._
import de.codecentric.applicative.Utils._
import fs2.Chunk
import mouse.boolean._

import scala.concurrent.ExecutionContext

trait Wc {

  def run(input: Iterator[Char]): (Int, Int, Int) = {
    implicit val ioContextShift: ContextShift[IO] =
      IO.contextShift(ExecutionContext.global)

    val ref = Ref.of[IO, Boolean](false).unsafeRunSync()
    val Tuple2K(Tuple2K(chars, lines), words) = fs2.Stream
      .fromIterator[IO, Char](input)
      .traverse_(c =>
          Tuple2K(Tuple2K(countChars[Unit](c), countLines[Unit](c)), countWords[Unit](ref)(c)))
      .compile.lastOrError.unsafeRunSync()

    (lines.getConst, words.value.unsafeRunSync().getConst, chars.getConst)
  }

  private[this] def runChunk(ref: Ref[IO, Boolean])(chunk: Chunk[Char]) = {
    chunk.traverse_ { c =>
      Tuple2K(Tuple2K(countChars[Unit](c), countLines[Unit](c)),
              countWords[Unit](ref)(c))
    }
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

object Wc extends Wc
