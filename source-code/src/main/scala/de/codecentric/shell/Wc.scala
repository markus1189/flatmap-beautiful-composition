package de.codecentric.shell

import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets

import scala.sys.process._

trait Wc {
  def run(iter: Iterator[Char]): (Int, Int, Int) = {
    val is = new ByteArrayInputStream(iter.mkString.getBytes(StandardCharsets.UTF_8))

    val output = (List("wc") #< is).!!
    output.split("""\s+""").dropWhile(_.forall(_.isWhitespace)).map(_.toInt) match {
      case Array(nw, nl, nc) => (nw, nl, nc)
      case r                 => throw new RuntimeException(s"Match error: ${r.toList} for given input. Output:\n$output\n-- end of output --")
    }
  }
}

object Wc extends Wc
