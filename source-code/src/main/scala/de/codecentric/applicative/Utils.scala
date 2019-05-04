package de.codecentric.applicative

import akka.stream.scaladsl.Sink
import cats.Applicative
import cats.syntax.applicative._
import cats.syntax.apply._
import fs2.Chunk

import scala.concurrent.Future

object Utils {
  def traverse_[A, B, F[_]: Applicative](input: Iterator[A])(
      f: A => F[B]): F[Unit] =
    input.foldLeft(Applicative[F].pure(()))(_ <* f(_))

  implicit class IteratorOps[A](iter: Iterator[A]) {
    def traverse_[B, F[_]: Applicative](f: A => F[B]): F[Unit] =
      Utils.traverse_(iter)(f)
  }

  implicit class ChunkOps[A](chunk: Chunk[A]) {
    def traverse_[B, F[_]: Applicative](f: A => F[B]): F[Unit] =
      chunk.foldLeft(Applicative[F].pure(()))((acc, x) => acc <* f(x))
  }

  implicit class Fs2StreamOps[A, G[_], F[_]](s: fs2.Stream[F, A]) {
    def traverse_[B](f: A => G[B])(
        implicit A: Applicative[G]): fs2.Stream[F, G[Unit]] =
      s.fold(Applicative[G].pure(()))(_ <* f(_))
  }

  def sinkTraverse_[F[_]: Applicative, A](
      f: A => F[Unit]): Sink[A, Future[F[Unit]]] =
    Sink.fold[F[Unit], A](().pure[F])((u, a) => u <* f(a))
}
