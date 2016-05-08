package com.pwootage.metroidprime.formats.mlvl

import com.pwootage.metroidprime.formats.BinarySerializable
import com.pwootage.metroidprime.formats.io.PrimeDataFile
import PrimeDataFile._

class MLVL extends BinarySerializable {

  import PrimeDataFile.Types._

  val header = new Header
  var memoryRelays = Array[MemoryRelay]()
  var areaUnk = u32
  var areas = Array[Area]()

  def write(f: PrimeDataFile): Unit = {
    f.write(header)
    if (header.prime1) f.write32(memoryRelays.length).writeArray(memoryRelays)
    f.write32(areas.length)
    if (header.prime1) f.write32(areaUnk)
    f.writeArray(areas)
  }

  def read(f: PrimeDataFile): Unit = {
    f.read(header)
    if (header.prime1) memoryRelays = f.readArray(f.read32(), () => new MemoryRelay)
    val areaCount = f.read32()
    if (header.prime1) areaUnk = f.read32()
    areas = f.readArray(areaCount, () => new Area(header.primeVersion _))
  }
}