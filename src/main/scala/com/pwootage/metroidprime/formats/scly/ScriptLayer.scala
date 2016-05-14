package com.pwootage.metroidprime.formats.scly

import com.pwootage.metroidprime.formats.BinarySerializable
import com.pwootage.metroidprime.formats.io.PrimeDataFile

class ScriptLayer extends BinarySerializable {

  var unk: Byte = -1
  var objects = Array[ScriptObjectInstance]()

  override def write(f: PrimeDataFile): Unit = {
    val start = f.pos
    f.write8(unk)
    f.write32(objects.length).writeArray(objects)
    f.writePaddingBytesGivenStartOffset(start, 32)
  }

  override def read(f: PrimeDataFile): Unit = {
    unk = f.read8()
    objects = f.readArray(f.read32(), () => new ScriptObjectInstance)
  }
}
