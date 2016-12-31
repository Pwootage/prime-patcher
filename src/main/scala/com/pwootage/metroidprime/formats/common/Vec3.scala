package com.pwootage.metroidprime.formats.common

import com.pwootage.metroidprime.formats.BinarySerializable
import com.pwootage.metroidprime.formats.io.PrimeDataFile

class Vec3 extends BinarySerializable {
  var x: Float = Float.NaN
  var y: Float = Float.NaN
  var z: Float = Float.NaN

  override def write(f: PrimeDataFile): Unit = {
    f.writeFloat(x)
    f.writeFloat(y)
    f.writeFloat(z)
  }

  override def read(f: PrimeDataFile): Unit = {
    x = f.readFloat()
    y = f.readFloat()
    z = f.readFloat()
  }

  def -(other: Vec3): Vec3 = {
    val res = new Vec3()
    res.x = x - other.x
    res.y = y - other.y
    res.z = z - other.z
    res
  }

  def x(other: Vec3): Vec3 = {
    val res = new Vec3()
    res.x = y * other.z - z * other.y
    res.y = x * other.z - z * other.x
    res.z = x * other.y - y * other.x
    res
  }

  def normalized: Vec3 = {
    val res = new Vec3()
    val len = Math.sqrt(x * x + y * y + z * z).toFloat
    res.x = x / len
    res.y = y / len
    res.z = z / len
    res
  }

  override def toString = s"Vec3($x, $y, $z)"
}
