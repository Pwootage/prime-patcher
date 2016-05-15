package com.pwootage.metroidprime.formats.dgrp

import com.pwootage.metroidprime.formats.BinarySerializable
import com.pwootage.metroidprime.formats.io.PrimeDataFile
import com.pwootage.metroidprime.utils.DataTypeConversion

class Resource extends BinarySerializable{
  var typ: Int = -1
  var id: Int = -1

  override def write(f: PrimeDataFile): Unit = {
    f.write32(id)
    f.write32(typ)
  }

  override def read(f: PrimeDataFile): Unit = {
    id = f.read32()
    typ = f.read32()
  }

  override def toString = s"Dependency(${id.toHexString}.${DataTypeConversion.intAsStr(typ)})"
}
