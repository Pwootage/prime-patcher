package com.pwootage.metroidprime.formats.mlvl

import com.pwootage.metroidprime.formats.BinarySerializable
import com.pwootage.metroidprime.formats.io.PrimeDataFile

class MLVL extends BinarySerializable {

  val header = new Header
  var memoryRelays = Array[MemoryRelay]()
  var areaUnk: Int = -1
  var areas = Array[Area]()
  var MAPWID: Int = -1
  var MAPWunk1: Byte = -1
  var MAPWunk2: Int = -1
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
    f.writePaddingTo(32)
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
    f.readPaddingTo(32)
  }
}