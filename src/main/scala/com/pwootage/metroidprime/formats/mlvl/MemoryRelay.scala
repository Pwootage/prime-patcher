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
    f.i32(relayInstanceID)
    f.i32(targetInstanceID)
    f.i16(message)
    f.i8(unk)
  }

  override def read(f: PrimeDataFile): Unit = {
    relayInstanceID = f.i32()
    targetInstanceID = f.i32()
    message = f.i16()
    unk = f.i8()
  }
}
