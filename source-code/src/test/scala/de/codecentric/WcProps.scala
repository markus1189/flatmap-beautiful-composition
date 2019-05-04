package de.codecentric

import java.io.{File, PrintWriter}

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import cats.instances.int._
import cats.instances.tuple._
import cats.syntax.eq._
import de.codecentric.applicative.stream.akka.Wc
import de.codecentric.applicative.stream.fs2
import org.scalacheck.Prop.BooleanOperators
import org.scalacheck.{Prop, Properties}

class WcProps extends Properties("WordCount") {
  propertyWithSeed("imperative vs shell", None) =
    wordCountProp("imperative")(imperative.Wc.run)

  propertyWithSeed("applicative-state vs shell", None) =
    wordCountProp("applicative-state")(applicative.Wc.run)

  propertyWithSeed("applicative-io vs shell", None) =
    wordCountProp("applicative-io")(applicative.io.Wc.run)

  propertyWithSeed("applicative-io-stream vs applicative", None) = {
    implicit val system: ActorSystem = ActorSystem()
    val mat: ActorMaterializer = ActorMaterializer()

    Prop.forAll(WcGen.text) { text =>
      val applic = applicative.io.Wc.run(text.iterator)
      val stream = new applicative.stream.akka.Wc {
        override def materializer: ActorMaterializer = mat
      }.run(text.iterator)

      val result = s"$stream === fs2 stream" |: s"$applic === applicative-io" |: applic == stream

      result
    }
  }

  private[this] def wordCountProp(propName: String)(
      run: Iterator[Char] => (Int, Int, Int)): Prop =
    Prop.forAll(WcGen.text) { text =>
      val expected =
        de.codecentric.shell.Wc.run(text.iterator)

      val actual = run(text.iterator)

      val result = s"$expected === expected" |: s"$actual === $propName" |: actual === expected

      result
    }

  private[this] def writeFile(text: String) = {
    val file = File.createTempFile("wcprop-file", ".txt")
    val writer = new PrintWriter(file)
    writer.write(text)
    writer.close()
    file
  }
}
