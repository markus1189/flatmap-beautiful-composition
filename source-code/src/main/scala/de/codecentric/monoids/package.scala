package de.codecentric

package object monoids {
  def wordsAndSkipped(input: Iterator[Char]): Iterator[(Int, String)] =
    new Iterator[(Int, String)] {
      override def hasNext: Boolean = input.hasNext

      override def next(): (Int, String) = {
        var skipped: Int = 0
        var current = input.next()
        //        if (input.hasNext) skipped += 1

        while (current.isSpaceChar) {
          skipped += 1
          current = input.next()
        }

        (skipped, current + input.takeWhile(!_.isWhitespace).mkString)
      }
    }
}
