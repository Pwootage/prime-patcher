package com.pwootage.metroidprime.formats.mlvl

import com.pwootage.metroidprime.formats.BinarySerializable
import com.pwootage.metroidprime.formats.io.PrimeDataFile

class MemoryRelay extends BinarySerializable {
  var relayInstanceID: Int = -1
  var targetInstanceID: Int = -1
  var message: Short = -1
  var unk: Byte = -1


  override def write(f: PrimeDataFile): Unit = {
    f.write32(relayInstanceID)
    f.write32(targetInstanceID)
    f.write16(message)
    f.write8(unk)
  }

  override def read(f: PrimeDataFile): Unit = {
    relayInstanceID = f.read32()
    targetInstanceID = f.read32()
    message = f.read16()
    unk = f.read8()
  }
}
