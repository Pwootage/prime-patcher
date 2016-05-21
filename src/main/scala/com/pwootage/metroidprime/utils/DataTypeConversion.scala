package com.pwootage.metroidprime.utils

object DataTypeConversion {
  def intContainingCharsAsStr(v: Int) = {
      val c1 = ((v >> 0) & 0xFF).toChar
      val c2 = ((v >> 8) & 0xFF).toChar
      val c3 = ((v >> 16) & 0xFF).toChar
      val c4 = ((v >> 24) & 0xFF).toChar
      new String(Array(c4, c3, c2, c1).filter(_ != 0))
  }

  def bytesToReadable(size: Long): String = {
    val bytes = size
    val kb = size.toDouble / 1000
    val mb = kb / 1000
    val byteString = bytes + " bytes"
    val kbString = kb.formatted("%.2f") + " KB"
    val mbString = mb.formatted("%.2f") + " MB"
    val sizeString = if (mb > 1) mbString else if (kb > 1) kbString else byteString
    sizeString
  }
}
