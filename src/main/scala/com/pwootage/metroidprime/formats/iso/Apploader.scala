package com.pwootage.metroidprime.formats.iso

import com.pwootage.metroidprime.formats.BinarySerializable
import com.pwootage.metroidprime.formats.io.PrimeDataFile

class Apploader extends BinarySerializable {
  var dateString = ""
  var entryPoint: Int = -1
  var trailerSize: Int = -1
  var code = Array[Byte]()

  override def write(f: PrimeDataFile): Unit = {
    f.writeString(dateString)
    f.writePaddingTo(0x10)
    f.write32(entryPoint)
    f.write32(code.length)
    f.write32(trailerSize)
    f.writeBytes(code)
  }

  override def read(f: PrimeDataFile): Unit = {
    dateString = f.readString()
    f.readPaddingTo(0x10)
    entryPoint = f.read32()
    val len = f.read32()
    trailerSize = f.read32()
    code = f.readBytes(len)
  }
}
