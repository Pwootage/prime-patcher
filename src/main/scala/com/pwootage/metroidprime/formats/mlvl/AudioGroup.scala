package com.pwootage.metroidprime.formats.mlvl

import com.pwootage.metroidprime.formats.BinarySerializable
import com.pwootage.metroidprime.formats.io.PrimeDataFile

class AudioGroup extends BinarySerializable {
  var unk1: Int = -1
  var AGSC: Int = -1

  override def write(f: PrimeDataFile): Unit = {
    f.write32(unk1)
    f.write32(AGSC)
  }

  override def read(f: PrimeDataFile): Unit = {
    unk1 = f.read32()
    AGSC = f.read32()
  }
}
