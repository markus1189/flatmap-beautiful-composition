package de.codecentric.defs

import cats.Functor

//snippet:applicative-def
trait Applicative[F[_]] extends Functor[F] {
  def pure[A](a: A): F[A]

  def ap[A, B](ff: F[A => B])(fa: F[A]): F[B]
}
//end
