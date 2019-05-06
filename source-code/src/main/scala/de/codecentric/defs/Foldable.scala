package de.codecentric.defs

import cats.Monoid

//snippet:foldable-def
trait Foldable[F[_]] {
  // rest omitted
  def foldMap[A, B](fa: F[A])(f: A => B)(implicit B: Monoid[B]): B
}
//end

object Foo { // to get a fresh scope
  //snippet:foldable-traverse
  trait Foldable[F[_]] {
    def traverse_[G[_], A, B](fa: F[A])(f: A => G[B])(
        implicit G: Applicative[G]): G[Unit]
  }
  //end
}
