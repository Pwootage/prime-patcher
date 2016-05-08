package com.pwootage.metroidprime.formats.mlvl

import com.pwootage.metroidprime.formats.BinarySerializable
import com.pwootage.metroidprime.formats.common.PrimeVersion
import com.pwootage.metroidprime.formats.dgrp.{DGRP, Resource}
import com.pwootage.metroidprime.formats.io.PrimeDataFile

class Area(val version: () => PrimeVersion) extends BinarySerializable {
  val header = new AreaHeader
  var attachedAreas = Array[Short]()
  val dependencies1 = new DGRP
  val dependencies2 = new DGRP
  var layerDependencyOffsets = Array[Int]()
  var docks = Array[Dock]()
  var relFiles = Array[String]()
  var relOffsets = Array[Int]()
  var areaName = ""

  override def write(f: PrimeDataFile): Unit = {
    f.write(header)
    f.write32(attachedAreas.length).writeArray(attachedAreas,  _.write16)
    f.write(dependencies1)
    f.write(dependencies2)
    f.write32(layerDependencyOffsets.length).writeArray(layerDependencyOffsets, _.write32)
    f.write32(docks.length).writeArray(docks)
    if (version() == PrimeVersion.PRIME_2) {
      f.write32(relFiles.length).writeArray(relFiles, _.writeString)
      f.write32(relOffsets.length).writeArray(relOffsets, _.write32)
      f.writeString(areaName)
    }
  }

  override def read(f: PrimeDataFile): Unit = {
    f.read(header)
    attachedAreas = f.readArrayWithCount(f.read32(), _.read16)
    f.read(dependencies1)
    f.read(dependencies2)
    layerDependencyOffsets = f.readArrayWithCount(f.read32(), _.read32)
    docks = f.readArray(f.read32(), () => new Dock)
    if (version() == PrimeVersion.PRIME_2) {
      relFiles = f.readArrayWithCount(f.read32(), _.readString)
      relOffsets = f.readArrayWithCount(f.read32(), _.read32)
      areaName = f.readString()
    }
  }
}
