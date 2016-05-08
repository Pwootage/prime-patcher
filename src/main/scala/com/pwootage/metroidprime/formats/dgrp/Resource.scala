package com.pwootage.metroidprime.formats.dgrp

import com.pwootage.metroidprime.formats.BinarySerializable
import com.pwootage.metroidprime.formats.io.PrimeDataFile

class Resource extends BinarySerializable{
  import PrimeDataFile.Types._
  var typ = u32
  var id = u32

  override def write(f: PrimeDataFile): Unit = {
    f.write32(id)
    f.write32(typ)
  }

  override def read(f: PrimeDataFile): Unit = {
    id = f.read32()
    typ = f.read32()
  }

  def typString = {
    val c1 = ((typ >> 0) & 0xFF).toChar
    val c2 = ((typ >> 8) & 0xFF).toChar
    val c3 = ((typ >> 16) & 0xFF).toChar
    val c4 = ((typ >> 24) & 0xFF).toChar
    new String(Array(c4, c3, c2, c1))
  }

  override def toString = s"Dependency(${id.toHexString}.$typString)"
}
