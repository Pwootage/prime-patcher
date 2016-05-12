package com.pwootage.metroidprime.formats.common

import com.pwootage.metroidprime.formats.BinarySerializable
import com.pwootage.metroidprime.formats.io.PrimeDataFile

class RGBA extends BinarySerializable {
  var r: Float = Float.NaN
  var g: Float = Float.NaN
  var b: Float = Float.NaN
  var a: Float = Float.NaN

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

  override def toString = s"RGBA($r, $g, $b, $a)"
}
