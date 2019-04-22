package de.codecentric.shell

import java.nio.file.Path

import scala.sys.process._

trait Wc {

  def run(path: Path): (Int, Int, Int) = {
    s"wc $path".!!.split("""\s+""").dropWhile(_.forall(_.isWhitespace)).dropRight(1).map(_.toInt) match {
      case Array(nw, nl, nc) => (nw, nl, nc)
      case r                 => throw new RuntimeException(s"Match error: ${r.toList} for file $path")
    }
  }
}

object Wc extends Wc
