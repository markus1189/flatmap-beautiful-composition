package de.codecentric.defs

import cats.data.{Const, Nested, Tuple2K, Validated}
import cats.effect.IO
import cats.instances.int._
import javax.swing.JFrame

class CompositionExample {
  //snippet:compose-applicative-monoid
  def example1Monoid: (Option[Int], Int) = (Option(1), 5)
  //end

  //snippet:compose-applicative-1
  def example1Applicative: Const[(Option[Int], Int), JFrame] =
    Const.of[JFrame]((Option(1), 5))
  //end

  //snippet:compose-applicative-2
  def example2: Nested[IO,
                       Tuple2K[Const[Int, ?], Validated[List[Throwable], ?], ?],
                       String] = {
    Nested {
      IO {
        Tuple2K(Const.of[String](1), Validated.valid("valid string"))
      }
    }
  }
  //end
}
