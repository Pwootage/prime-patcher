package com.pwootage.metroidprime.formats.mlvl

import com.pwootage.metroidprime.formats.BinarySerializable
import com.pwootage.metroidprime.formats.io.PrimeDataFile

class ConnectingDock extends BinarySerializable {

  import PrimeDataFile.Types._

  var area = u32
  var dock = u32

  override def write(f: PrimeDataFile): Unit = {
    f.write32(area)
    f.write32(dock)
  }

  override def read(f: PrimeDataFile): Unit = {
    area = f.read32()
    dock = f.read32()
  }

  override def toString = s"ConnectingDock($area, $dock)"
}
