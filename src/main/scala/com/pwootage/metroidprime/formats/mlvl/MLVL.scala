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
  var MAPWID = u32
  var MAPWunk1 = u8
  var MAPWunk2 = u32
  var audioGroups = Array[AudioGroup]()
  var audioUnk1 = ""
  var layerFlags = Array[LayerFlags]()
  var layerNames = Array[String]()
  var layerOffsets = Array[Int]()

  def write(f: PrimeDataFile): Unit = {
    f.write(header)
    if (header.prime1) f.write32(memoryRelays.length).writeArray(memoryRelays)
    f.write32(areas.length)
    if (header.prime1) f.write32(areaUnk)
    f.writeArray(areas)
    f.write32(MAPWID)
    f.write8(MAPWunk1)
    f.write32(MAPWunk2)
    if (header.prime1) {
      f.write32(audioGroups.length).writeArray(audioGroups)
      f.writeString(audioUnk1)
    }
    f.write32(layerFlags.length).writeArray(layerFlags)
    f.write32(layerNames.length).writeArray(layerNames, _.writeString)
    f.write32(layerOffsets.length).writeArray(layerOffsets, _.write32)
    val endPos = f.pos
    val padding = 32 - (endPos % 32).toInt
    if (padding != 32) {
      for ( _ <- 1 to padding) f.write8(0xFF.toByte)
    }
  }

  def read(f: PrimeDataFile): Unit = {
    f.read(header)
    if (header.prime1) memoryRelays = f.readArray(f.read32(), () => new MemoryRelay)
    val areaCount = f.read32()
    if (header.prime1) areaUnk = f.read32()
    areas = f.readArray(areaCount, () => new Area(header.primeVersion _))
    MAPWID = f.read32()
    MAPWunk1 = f.read8()
    MAPWunk2 = f.read32()
    if (header.prime1) {
      audioGroups = f.readArray(f.read32(), () => new AudioGroup)
      audioUnk1 = f.readString()
    }
    layerFlags = f.readArray(f.read32(), () => new LayerFlags)
    layerNames = f.readArrayWithCount(f.read32(), _.readString)
    layerOffsets = f.readArrayWithCount(f.read32(), _.read32)
    val endPos = f.pos
    val padding = 32 - (endPos % 32).toInt
    if (padding != 32) {
      for ( _ <- 1 to padding) f.read8()
    }
  }
}