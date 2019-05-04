package de.codecentric.defs

import cats.kernel.Monoid
import cats.syntax.monoid._

//snippet:const-def
final case class Const[A, B](getConst: A)
//end

object Const {
  //snippet:const-applicative
  implicit def constApplicative[X: Monoid]: Applicative[Const[X, ?]] =
    new Applicative[Const[X, ?]] {
      override def map[A, B](fa: Const[X, A])(f: A => B): Const[X, B] =
        Const(fa.getConst)

      override def pure[A](a: A): Const[X, A] = Const(a)

      override def ap[A, B](ff: Const[X, A => B])(
          fa: Const[X, A]): Const[X, B] = Const(ff.getConst |+| fa.getConst)
    }
  //end

}
