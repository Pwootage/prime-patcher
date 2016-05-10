package com.pwootage.metroidprime.formats.common

import com.pwootage.metroidprime.formats.BinarySerializable
import com.pwootage.metroidprime.formats.io.PrimeDataFile

class Face extends BinarySerializable {
  var ind1: Short = -1
  var ind2: Short = -1
  var ind3: Short = -1

  override def write(f: PrimeDataFile): Unit = {
    f.write16(ind1)
    f.write16(ind2)
    f.write16(ind3)
  }

  override def read(f: PrimeDataFile): Unit = {
    ind1 = f.read16()
    ind2 = f.read16()
    ind3 = f.read16()
  }

  override def toString = s"Face($ind1, $ind2, $ind3)"
}
