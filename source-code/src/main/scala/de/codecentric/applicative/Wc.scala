package de.codecentric.applicative

import cats.data.{Const, Nested, State, Tuple2K}
import cats.instances.int._
import de.codecentric.applicative.Utils._
import mouse.boolean._

trait Wc {
  def run(input: Iterator[Char]): (Int, Int, Int) = {
    def countChars[A](c: Char): Const[Int, A] = Const.of(1)
    def countLines[A](c: Char): Const[Int, A] = (c == '\n') ?? Const.of[A](1)
    //snippet:count-words-state
    def countWords[A](c: Char): Nested[State[Boolean, ?], Const[Int, ?], A] =
      Nested {
        for {
          before <- State.get[Boolean]
          after = !c.isWhitespace
          _ <- State.set[Boolean](after)
        } yield {
          (!before && after) ?? Const.of[A](1)
        }
      }
    //end

    val Tuple2K(Tuple2K(chars_, lines), words_) = input.traverse_ { c =>
      Tuple2K(Tuple2K(countChars[Unit](c), countLines[Unit](c)),
              countWords[Unit](c))
    }

    (lines.getConst, words_.value.runA(false).value.getConst, chars_.getConst)
  }
}

object Wc extends Wc
