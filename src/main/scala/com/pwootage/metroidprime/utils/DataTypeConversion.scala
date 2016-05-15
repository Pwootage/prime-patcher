package com.pwootage.metroidprime.utils

object DataTypeConversion {
  def intAsStr(v: Int) = {
      val c1 = ((v >> 0) & 0xFF).toChar
      val c2 = ((v >> 8) & 0xFF).toChar
      val c3 = ((v >> 16) & 0xFF).toChar
      val c4 = ((v >> 24) & 0xFF).toChar
      new String(Array(c4, c3, c2, c1).filter(_ != 0))
  }

}
