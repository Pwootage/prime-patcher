package com.pwootage.metroidprime.templates

import com.pwootage.metroidprime.formats.BinarySerializable
import com.pwootage.metroidprime.formats.io.PrimeDataFile

class ColorScriptPropertyValue extends BinarySerializable {
  var r: Float = 0
  var g: Float = 0
  var b: Float = 0
  var a: Float = 1

  def this(str: String) {
    this()
    val split = str.split(",").map(_.trim)
    r = split(0).toFloat
    g = split(1).toFloat
    b = split(2).toFloat
    a = split(3).toFloat
  }

  override def toString = s"ColorScriptPropertyValue()"

  override def write(f: PrimeDataFile): Unit = {
    f.writeFloat(r)
    f.writeFloat(g)
    f.writeFloat(b)
    f.writeFloat(a)
  }

  override def read(f: PrimeDataFile): Unit = {
    r = f.readFloat()
    g = f.readFloat()
    b = f.readFloat()
    a = f.readFloat()
  }

}
