package com.pwootage.metroidprime.formats.mlvl

import com.pwootage.metroidprime.formats.BinarySerializable
import com.pwootage.metroidprime.formats.io.PrimeDataFile
import PrimeDataFile._

class MLVL extends BinarySerializable {
  val header = new Header
  var memoryRelays = Array[MemoryRelay]()

  def write(f: PrimeDataFile): Unit = {
    header.write(f)
  }

  def read(f: PrimeDataFile): Unit = {
    header.read(f)
    if (header.prime1) {
      val count = f.i32()
      memoryRelays = Array.fill(count)(new MemoryRelay)
      for (m <- memoryRelays) m.read(f)
    }
  }
}