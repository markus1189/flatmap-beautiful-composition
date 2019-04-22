package de.codecentric.applicative

import cats.Applicative
import cats.syntax.apply._

object Utils {
  def traverse_[A, B, F[_]: Applicative](input: Iterator[A])(
      f: A => F[B]): F[Unit] = {
    var acc = Applicative[F].pure(())

    while (input.hasNext) {
      val next = f(input.next())
      acc = acc <* next
    }

    acc
  }

  implicit class IteratorOps[A](iter: Iterator[A]) {
    def traverse_[B, F[_]: Applicative](f: A => F[B]): F[Unit] =
      Utils.traverse_(iter)(f)
  }
}
