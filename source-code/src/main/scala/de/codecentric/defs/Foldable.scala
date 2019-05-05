package de.codecentric.defs

import cats.Monoid

//snippet:foldable-def
trait Foldable[F[_]] {
  // rest omitted
  def foldMap[A, B](fa: F[A])(f: A => B)(implicit B: Monoid[B]): B
}
//end
