package de.codecentric

import org.scalacheck.Gen

trait WcGen {
  val word: Gen[String] = for {
    c1 <- Gen.alphaChar
    c2 <- Gen.alphaChar
    w <- Gen.alphaStr
  } yield c1 + c2 + w

  val sentence: Gen[String] = Gen.nonEmptyListOf(word).map(_.mkString(" ")).map(_ + ".")

  val line: Gen[String] = for {
    s <- sentence
    nls <- Gen.oneOf(Seq("\n", "\n\n", "\r\n"))
  } yield s + nls

  val text: Gen[String] = Gen.nonEmptyListOf(line).map(_.mkString + "\n")
}

object WcGen extends WcGen