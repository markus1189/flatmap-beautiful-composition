package de.codecentric

import java.io.{File, PrintWriter}

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import cats.instances.int._
import cats.instances.tuple._
import cats.syntax.eq._
import de.codecentric.applicative.stream.akka.Wc
import org.scalacheck.Prop.BooleanOperators
import org.scalacheck.{Prop, Properties}

class WcProps extends Properties("WordCount") {
  propertyWithSeed("imperative vs shell", None) =
    wordCountProp("imperative")(imperative.Wc.run)

  propertyWithSeed("applicative-state vs shell", None) =
    wordCountProp("applicative-state")(applicative.Wc.run)

  propertyWithSeed("applicative-io vs shell", None) =
    wordCountProp("applicative-io")(applicative.io.Wc.run)

  propertyWithSeed("applicative-io-stream vs shell", None) = {
    implicit val system: ActorSystem = ActorSystem()
    val mat: ActorMaterializer = ActorMaterializer()

    val wc = new Wc {
      override def materializer: ActorMaterializer = mat
    }

      wordCountProp("applicative-io-stream")(wc.run)
    }

  private[this] def wordCountProp(propName: String)(
      run: Iterator[Char] => (Int, Int, Int)): Prop =
    Prop.forAll(WcGen.text) { text =>
      val file: File = writeFile(text)

      val expected =
        de.codecentric.shell.Wc.run(file.toPath.toAbsolutePath)

      val actual = run(text.iterator)

      val result = s"$expected === expected" |: s"$actual === $propName" |: actual === expected

      file.delete() // DON'T delete on exception

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
