package com.pwootage.metroidprime.formats.mlvl

import com.pwootage.metroidprime.formats.BinarySerializable
import com.pwootage.metroidprime.formats.io.PrimeDataFile

class AreaHeader extends BinarySerializable{
  import PrimeDataFile.Types._

  var name = u32
  val transform = floatArray(12)
  val boundingBox = floatArray(6)
  var MREA = u32
  var id = u32

  override def write(f: PrimeDataFile): Unit = {
    f.write32(name)
    f.writeArray(transform, _.writeFloat)
    f.writeArray(boundingBox, _.writeFloat)
    f.write32(MREA)
    f.write32(id)
  }

  override def read(f: PrimeDataFile): Unit = {
    name = f.read32()
    f.readArray(transform, _.readFloat)
    f.readArray(boundingBox, _.readFloat)
    MREA = f.read32()
    id = f.read32()
  }
}
