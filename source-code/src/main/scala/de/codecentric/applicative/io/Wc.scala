package de.codecentric.applicative.io

import cats.data.{Const, Nested, Tuple2K}
import cats.effect.IO
import cats.effect.concurrent.Ref
import cats.instances.int._
import de.codecentric.applicative.Utils._
import mouse.boolean._

trait Wc {
  def run(input: Iterator[Char]): (Int, Int, Int) = {
    def countChars[A](c: Char): Const[Int, A] = Const.of(1)
    def countLines[A](c: Char): Const[Int, A] = (c == '\n') ?? Const.of[A](1)
    def countWords[A](ref: Ref[IO, Boolean])(c: Char) = Nested {
      for {
        before <- ref.get
        after = !c.isWhitespace
        _ <- ref.set(after)
      } yield (!before && after) ?? Const.of[A](1)
    }

    val ref = Ref.of[IO, Boolean](false).unsafeRunSync

    val Tuple2K(Tuple2K(chars_, lines), words) = input.traverse_ { c =>
      Tuple2K(Tuple2K(countChars[Unit](c), countLines[Unit](c)),
              countWords[Unit](ref)(c))
    }

    (lines.getConst, words.value.unsafeRunSync().getConst, chars_.getConst)
  }
}

object Wc extends Wc
