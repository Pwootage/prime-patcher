package com.pwootage.metroidprime.formats.pak

import com.pwootage.metroidprime.formats.BinarySerializable
import com.pwootage.metroidprime.formats.io.PrimeDataFile
import com.pwootage.metroidprime.utils.DataTypeConversion

class Resource extends BinarySerializable {
  var compressed = false
  var typ: Int = -1
  var id: Int = -1
  var size: Int = -1
  var offset: Int = -1

  override def write(f: PrimeDataFile): Unit = {
    f.write32(if (compressed) 1 else 0)
    f.write32(typ)
    f.write32(id)
    f.write32(size)
    f.write32(offset)
  }

  override def read(f: PrimeDataFile): Unit = {
    compressed = f.read32() > 0
    typ = f.read32()
    id = f.read32()
    size = f.read32()
    offset = f.read32()
  }

  def idStr = DataTypeConversion.intPrimeResourceNameToStr(id, typ)
}
