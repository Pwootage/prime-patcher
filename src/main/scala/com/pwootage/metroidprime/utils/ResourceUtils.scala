package com.pwootage.metroidprime.utils

import java.io.FileNotFoundException

import better.files._

object ResourceUtils {
  def resourceAsString(path: String): String = {
    val in = getClass.getResourceAsStream(path)
    if (in == null) {
      throw new FileNotFoundException(path)
    }
    new String(in.bytes.toArray)
  }

  def resourceAsBytes(path: String): Array[Byte] = {
    val in = getClass.getResourceAsStream(path)
    if (in == null) {
      throw new FileNotFoundException(path)
    }
    in.bytes.toArray
  }
}
