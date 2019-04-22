package de.codecentric.imperative

/**
  * Naive Translation from:
  * Gibbons, Jeremy, and Bruno C. D. S. Oliveira. "The essence of the iterator pattern." Journal of functional programming 19.3-4 (2009): 377-402.
  */
trait Wc {
//snippet:imperative-wc
  def run(input: Iterator[Char]): (Int, Int, Int) = {
    var (nl, nw, nc) = (0, 0, 0)
    var state = false

    input.foreach { c =>
      nc += 1
      if (c == '\n') nl += 1
      if (c == ' ' || c == '\n' || c == '\t') {
        state = false
      } else if (!state) {
        state = true
        nw += 1
      }
    }

    (nl, nw, nc)
  }
//end
}

object Wc extends Wc
