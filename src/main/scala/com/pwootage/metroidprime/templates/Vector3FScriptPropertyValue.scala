package com.pwootage.metroidprime.templates

import com.pwootage.metroidprime.formats.BinarySerializable
import com.pwootage.metroidprime.formats.io.PrimeDataFile

class Vector3FScriptPropertyValue extends BinarySerializable {
  var x: Float = 0
  var y: Float = 0
  var z: Float = 0

  def this(str: String) {
    this()
    val split = str.split(",").map(_.trim)
    x = split(0).toFloat
    y = split(1).toFloat
    z = split(2).toFloat
  }


  override def toString = s"Vector3FScriptPropertyValue($x, $y, $z)"

  override def write(f: PrimeDataFile): Unit = {
    f.writeFloat(x)
    f.writeFloat(y)
    f.writeFloat(z)
  }
  override def read(f: PrimeDataFile): Unit = {
    x = f.readFloat()
    y = f.readFloat()
    z = f.readFloat()
  }
}
