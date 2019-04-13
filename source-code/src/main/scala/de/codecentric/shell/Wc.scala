package de.codecentric.shell

import java.nio.file.Path

import scala.sys.process._

trait Wc {

  def run(path: Path): (Int, Int, Int) = {
    val Array(nw, nl, nc) =
      s"wc $path".!!.split("""\s+""").drop(1).dropRight(1).map(_.toInt)

    (nw, nl, nc)
  }
}

object Wc extends Wc
