package de.codecentric.defs

import cats.{Foldable, Functor}

//snippet:traverse-def
trait Traverse[F[_]] extends Functor[F] with Foldable[F] {
  def traverse[G[_]: Applicative, A, B](fa: F[A])(f: A => G[B]): G[F[B]]
}
//end
