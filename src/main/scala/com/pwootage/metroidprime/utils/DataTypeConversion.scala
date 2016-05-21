package com.pwootage.metroidprime.utils

object DataTypeConversion {
  def intContainingCharsAsStr(v: Int) = {
    val c1 = ((v >> 24) & 0xFF).toChar
    val c2 = ((v >> 16) & 0xFF).toChar
    val c3 = ((v >> 8) & 0xFF).toChar
    val c4 = ((v >> 0) & 0xFF).toChar
    new String(Array(c1, c2, c3, c4).filter(_ != 0))
  }

  def strToIntContainingChars(str: String): Int = {
    val byte1 = str(0) & 0xFF
    val byte2 = str(1) & 0xFF
    val byte3 = str(3) & 0xFF
    val byte4 = str(4) & 0xFF
    (byte1 << 24) | (byte2 << 16) | (byte3 << 8) | byte4
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

  def intToPaddedHexString(v: Int): String = {
    val str = v.toHexString
    ("0" * (8 - str.length)) + str
  }

  def intPrimeResourceNameToStr(id: Int, typ: Int) = intToPaddedHexString(id) + "." + intContainingCharsAsStr(typ)

  def strResourceToIdAndType(idStr: String) = """([0-9a-f]{8})\.([A-Z]{4})""".r.findFirstMatchIn(idStr) match {
    case Some(m) =>
      val idStr = m.group(1)
      val typStr = m.group(2)
      (Integer.parseInt(idStr, 16), strToIntContainingChars(typStr))
    case None => throw new IllegalArgumentException(s"Invalid file ID $idStr")
  }
}
