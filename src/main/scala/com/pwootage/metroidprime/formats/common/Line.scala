package com.pwootage.metroidprime.formats.common

import com.pwootage.metroidprime.formats.BinarySerializable
import com.pwootage.metroidprime.formats.io.PrimeDataFile

class Line extends BinarySerializable {
  var ind1: Short = -1
  var ind2: Short = -1

  override def write(f: PrimeDataFile): Unit = {
    f.write16(ind1)
    f.write16(ind2)
  }

  override def read(f: PrimeDataFile): Unit = {
    ind1 = f.read16()
    ind2 = f.read16()
  }

  override def toString = s"Line($ind1, $ind2)"
}
