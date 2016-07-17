package com.pwootage.metroidprime.formats.mrea

import com.pwootage.metroidprime.formats.BinarySerializable
import com.pwootage.metroidprime.formats.io.PrimeDataFile

class MREACompressedBlockHeader extends BinarySerializable {
  var bufferSize: Int = -1
  var uncompressedSize: Int = -1
  var compressedSize: Int = -1
  var dataSectionCount: Int = -1

  override def write(f: PrimeDataFile): Unit = {
    f.write32(bufferSize)
    f.write32(uncompressedSize)
    f.write32(compressedSize)
    f.write32(dataSectionCount)
  }

  override def read(f: PrimeDataFile): Unit = {
    bufferSize = f.read32()
    uncompressedSize = f.read32()
    compressedSize = f.read32()
    dataSectionCount = f.read32()
  }
}
