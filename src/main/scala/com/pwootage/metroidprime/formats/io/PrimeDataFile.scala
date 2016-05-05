package com.pwootage.metroidprime.formats.io

import java.io.{DataInputStream, InputStream, RandomAccessFile}

object PrimeDataFile {
  object Types {
    def u8: Byte = -1
    def u8(v: Byte) = v
    def u16: Short = -1
    def u16(v:Short): Short = v
    def u32: Int = -1
    def u32(v:Int): Int = v
    def u64: Long = -1
    def u64(v:Long) = v
    def float: Float = Float.NaN
    def float(v:Float): Float = v
    def bool: Boolean = false
    def bool(v: Boolean) = v
  }
}

class PrimeDataFile(raf: RandomAccessFile) {
  def i8(): Byte = raf.readByte()
  def i8(v: Byte) = raf.writeByte(v)
  def i16(): Short = raf.readShort()
  def i16(v:Short) = raf.writeShort(v)
  def i32(): Int = raf.readInt()
  def i32(v: Int) = raf.writeInt(v)
  def i64(): Long = raf.readLong()
  def i64(v: Long) = raf.writeLong(v)
  def bool(): Boolean = raf.readByte() > 0
  def bool(v:Boolean) = raf.write(if (v) 1 else 0)
  def float(): Float = raf.readFloat()
  def float(v:Float) = raf.writeFloat(v)

  def close() = raf.close()
}
