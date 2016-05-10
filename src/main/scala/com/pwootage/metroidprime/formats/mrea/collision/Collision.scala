package com.pwootage.metroidprime.formats.mrea.collision

import com.pwootage.metroidprime.formats.BinarySerializable
import com.pwootage.metroidprime.formats.common.{Face, Line, Vec3}
import com.pwootage.metroidprime.formats.io.PrimeDataFile

object Collision {
  val MAGIC = 0xDEAFBABE
}

class Collision extends BinarySerializable {
  var unk1: Long = -1
  var magic: Int = -1
  var version: Int = -1
  val boundingBox = new Array[Float](6)
  var unk2: Int = -1
  var octree = Array[Byte]()
  var collisionMaterialFlags = Array[Int]()
  var vertPropertyIndices = Array[Byte]()
  var linePropertyIndices = Array[Byte]()
  var facePropertyIndices = Array[Byte]()
  var lines = Array[Line]()
  var faces = Array[Face]()
  var verts = Array[Vec3]()

  override def write(f: PrimeDataFile): Unit = {

  }

  override def read(f: PrimeDataFile): Unit = {
    unk1 = f.read64()
    magic = f.read32()
    version = f.read32()
    f.readArray(boundingBox, _.readFloat)
    unk2 = f.read32()
    octree = f.readBytes(f.read32())
    collisionMaterialFlags = f.readArrayWithCount(f.read32(), _.read32)
    vertPropertyIndices = f.readBytes(f.read32())
    linePropertyIndices = f.readBytes(f.read32())
    facePropertyIndices = f.readBytes(f.read32())
    lines = f.readArray(f.read32(), () => new Line)
    faces = f.readArray(f.read32() / 3, () => new Face)
    verts = f.readArray(f.read32(), () => new Vec3)
    //f.readPaddingTo(32) //Should I?

    if (magic != Collision.MAGIC) {
      throw new IllegalArgumentException(s"Invalid magic: ${magic.toHexString}")
    }
  }
}
