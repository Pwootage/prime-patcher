package com.pwootage.metroidprime.formats.mrea

import java.io.{ByteArrayInputStream, DataInputStream}

import com.pwootage.metroidprime.formats.BinarySerializable
import com.pwootage.metroidprime.formats.io.PrimeDataFile
import com.pwootage.metroidprime.formats.mrea.collision.Collision
import com.pwootage.metroidprime.formats.scly.SCLY

object MREA {
  val MAGIC = 0xDEADBEEF
  val VERSION_PRIME_1 = 0xF
  val VERSION_PRIME_2 = 0x19
}

class MREA extends BinarySerializable {
  var magic: Int = -1
  var version: Int = -1
  val transform = new Array[Float](12)
  var meshCount: Int = -1
  var sectionCount: Int = -1
  var geometrySection: Int = -1
  var SCLYSection: Int = -1
  var collisionSection: Int = -1
  var unknownSection1: Int = -1
  var lightSection: Int = -1
  var VISISection: Int = -1
  var PATHSection: Int = -1
  var AROTsection: Int = -1
  var sectionSizes = Array[Int]()
  var rawSections = Array[Array[Byte]]()

  override def write(f: PrimeDataFile): Unit = {
    f.write32(magic)
    f.write32(version)
    f.writeArray(transform, _.writeFloat)
    f.write32(meshCount)
    f.write32(sectionCount)
    f.write32(geometrySection)
    f.write32(SCLYSection)
    f.write32(collisionSection)
    f.write32(unknownSection1)
    f.write32(lightSection)
    f.write32(VISISection)
    f.write32(PATHSection)
    f.write32(AROTsection)
    f.writeArray(sectionSizes, _.write32)
    f.writePaddingTo(32)

    for (i <- rawSections) {
      f.writeBytes(i)
      f.writePaddingTo(32)
    }
  }

  override def read(f: PrimeDataFile): Unit = {
    magic = f.read32()
    version = f.read32()
    f.readArray(transform, _.readFloat)
    meshCount = f.read32()
    sectionCount = f.read32()
    geometrySection = f.read32()
    SCLYSection = f.read32()
    collisionSection = f.read32()
    unknownSection1 = f.read32()
    lightSection = f.read32()
    VISISection = f.read32()
    PATHSection = f.read32()
    AROTsection = f.read32()
    sectionSizes = f.readArrayWithCount(sectionCount, _.read32)
    f.readPaddingTo(32)

    rawSections = new Array(sectionCount)
    for (i <- rawSections.indices) {
      rawSections(i) = f.readBytes(sectionSizes(i))
      f.readPaddingTo(32)
    }

    if (magic != MREA.MAGIC) {
      throw new IllegalArgumentException(s"Invalid magic: ${magic.toHexString}")
    }
    if (!(version == MREA.VERSION_PRIME_1 || version == MREA.VERSION_PRIME_2)) {
      throw new IllegalArgumentException(s"Invalid version: ${version.toHexString}")
    }
  }

  def parseCollision = {
    val res = new Collision
    res.read(rawSections(collisionSection))
    res
  }

  def parseSCLY = {
    val res = new SCLY
    res.read(rawSections(SCLYSection))
    res
  }
}
