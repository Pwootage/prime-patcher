package com.pwootage.metroidprime.formats.io

import java.io.RandomAccessFile
import java.nio.charset.StandardCharsets
import java.util

import com.pwootage.metroidprime.formats.BinarySerializable

import scala.reflect.ClassTag

object PrimeDataFile {

  object Types {
    def u8: Byte = -1
    def u8(v: Byte) = v
    def u16: Short = -1
    def u16(v: Short): Short = v
    def u32: Int = -1
    def u32(v: Int): Int = v
    def u64: Long = -1
    def u64(v: Long) = v
    def float: Float = Float.NaN
    def float(v: Float): Float = v
    def floatArray(n: Int): Array[Float] = Array.fill(n)(Float.NaN)
    def bool: Boolean = false
    def bool(v: Boolean) = v
  }

}

class PrimeDataFile(raf: RandomAccessFile) {
  def read8(): Byte = raf.readByte()
  def write8(v: Byte) = {raf.writeByte(v); this}
  def read16(): Short = raf.readShort()
  def write16(v: Short) = {raf.writeShort(v); this}
  def read32(): Int = raf.readInt()
  def write32(v: Int) = {raf.writeInt(v); this}
  def read64(): Long = raf.readLong()
  def write64(v: Long) = {raf.writeLong(v); this}
  def readBool(): Boolean = raf.readByte() > 0
  def writeBool(v: Boolean) = {this.write8(if (v) 1 else 0); this}
  def readFloat(): Float = raf.readFloat()
  def writeFloat(v: Float) = {raf.writeFloat(v); this}

  def write[T <: BinarySerializable](i: T) = {i.write(this); this}
  def read[T <: BinarySerializable](i: T): T = {i.read(this); i}

  def readString(): String = {
    val str = new Array[Byte](512)
    for (i <- str.indices) {
      val read = read8()
      if (read == 0) {
        return new String(str.slice(0, i), StandardCharsets.US_ASCII)
      } else {
        str(i) = read
      }
    }
    throw new IllegalArgumentException("Too long of a string! (>512 bytes)")
  }

  def writeString(s: String) = {
    this.writeArray(s.getBytes(StandardCharsets.US_ASCII), _.write8).write8(0)
  }

  def readArray[T <: BinarySerializable : ClassTag](n: Int, create: () => T): Array[T] = {
    val res = new Array[T](n)
    for (i <- res.indices) {
      res(i) = create()
      res(i).read(this)
    }
    res
  }

  def readArray[T <: BinarySerializable](items: Array[T]): Array[T] = {
    for (i <- items) i.read(this)
    items
  }

  def writeArray[T <: BinarySerializable](items: Array[T]) = {
    for (i <- items) i.write(this)
    this
  }

  def readArrayWithCount[T: ClassTag](n: Int, method: PrimeDataFile => () => T): Array[T] = {
    val m = method(this)
    val items = new Array[T](n)
    for (i <- items.indices) items(i) = m()
    items
  }

  def readArray[T](items: Array[T], method: PrimeDataFile => () => T): Array[T] = {
    val m = method(this)
    for (i <- items.indices) items(i) = m()
    items
  }

  def writeArray[T](items: Array[T], method: PrimeDataFile => (T) => PrimeDataFile) = {
    val m = method(this)
    for (i <- items) m(i)
    this
  }

  def pos = raf.getFilePointer

  def close() = raf.close()
}
