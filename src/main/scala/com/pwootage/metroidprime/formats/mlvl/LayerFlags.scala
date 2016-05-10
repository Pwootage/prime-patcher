package com.pwootage.metroidprime.formats.mlvl

import com.pwootage.metroidprime.formats.BinarySerializable
import com.pwootage.metroidprime.formats.io.PrimeDataFile

class LayerFlags extends BinarySerializable {
  var count: Int = -1
  var flags: Long = 0xDEADBEA7l

  override def write(f: PrimeDataFile): Unit = {
    f.write32(count)
    f.write64(flags)
  }

  override def read(f: PrimeDataFile): Unit = {
    count = f.read32()
    flags = f.read64()
  }
}
