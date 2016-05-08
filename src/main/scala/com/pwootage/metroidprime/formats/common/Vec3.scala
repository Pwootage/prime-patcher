package com.pwootage.metroidprime.formats.common

import com.pwootage.metroidprime.formats.BinarySerializable
import com.pwootage.metroidprime.formats.io.PrimeDataFile

class Vec3 extends BinarySerializable {
  import PrimeDataFile.Types._

  var x = float
  var y = float
  var z = float

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

  override def toString = s"Vec3($x, $y, $z)"
}
