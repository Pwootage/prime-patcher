package com.pwootage.metroidprime.formats.pak

import java.nio.charset.StandardCharsets

import com.pwootage.metroidprime.formats.BinarySerializable
import com.pwootage.metroidprime.formats.io.PrimeDataFile
import com.pwootage.metroidprime.utils.DataTypeConversion

class NamedResource extends BinarySerializable {
  var typ: Int = -1
  var id: Int = -1
  var name: String = ""


  override def write(f: PrimeDataFile): Unit = {
    f.write32(typ)
    f.write32(id)
    f.write32(name.length).writeBytes(name.getBytes(StandardCharsets.US_ASCII))
  }

  override def read(f: PrimeDataFile): Unit = {
    typ = f.read32()
    id = f.read32()
    name = new String(f.readBytes(f.read32()), StandardCharsets.US_ASCII)
  }

  def idStr = DataTypeConversion.intPrimeResourceNameToStr(id, typ)
}
