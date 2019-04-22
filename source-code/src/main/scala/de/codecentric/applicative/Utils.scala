package de.codecentric.applicative

import akka.stream.scaladsl.Sink
import cats.Applicative
import cats.syntax.applicative._
import cats.syntax.apply._

import scala.concurrent.Future

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

  def sinkTraverse_[F[_]: Applicative, A](
      f: A => F[Unit]): Sink[A, Future[F[Unit]]] =
    Sink.fold[F[Unit], A](().pure[F])((u, a) => u <* f(a))
}
