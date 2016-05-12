package com.pwootage.metroidprime.formats.scly.prime1ScriptObjects.common

import com.pwootage.metroidprime.formats.BinarySerializable
import com.pwootage.metroidprime.formats.io.PrimeDataFile

class VisorParams extends BinarySerializable {
  var unknown1: Boolean = false
  var unknown2: Boolean = false
  var flags: Int = -1

  override def write(f: PrimeDataFile): Unit = {
    f.writeBool(unknown1)
    f.writeBool(unknown2)
    f.write32(flags)
  }

  override def read(f: PrimeDataFile): Unit = {
    unknown1 = f.readBool()
    unknown2 = f.readBool()
    flags = f.read32()
  }
}
