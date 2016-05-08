package com.pwootage.metroidprime.formats.mlvl

import com.pwootage.metroidprime.formats.BinarySerializable
import com.pwootage.metroidprime.formats.io.PrimeDataFile

class MemoryRelay extends BinarySerializable {
  import PrimeDataFile.Types._
  var relayInstanceID = u32
  var targetInstanceID = u32
  var message = u16
  var unk = u8(0)


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
