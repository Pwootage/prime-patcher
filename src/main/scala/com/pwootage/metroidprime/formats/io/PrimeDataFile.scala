package com.pwootage.metroidprime.formats.io

import java.io._
import java.nio.charset.StandardCharsets
import java.util

import com.pwootage.metroidprime.formats.BinarySerializable

import scala.reflect.ClassTag

class PrimeDataFile(input: Option[DataInput], output: Option[DataOutput]) {

  def this(bytes: Array[Byte]) {
    this(Some(new DataInputStream(new ByteArrayInputStream(bytes))), None)
  }

  def this(dataInput: DataInput) {
    this(Some(dataInput), None)
  }

  def this(dataOutput: DataOutput) {
    this(None, Some(dataOutput))
  }

  def this(dataInput: DataInput, dataOutput: DataOutput) {
    this(Some(dataInput), Some(dataOutput))
  }

  private lazy val in = input.get
  private lazy val out = output.get
  private var _pos: Long = 0

  def read8(): Byte = {_pos += 1; in.readByte()}
  def write8(v: Byte) = {_pos += 1; out.writeByte(v); this}
  def read16(): Short = {_pos += 2; in.readShort()}
  def write16(v: Short) = {_pos += 2; out.writeShort(v); this}
  def read32(): Int = {_pos += 4; in.readInt()}
  def write32(v: Int) = {_pos += 4; out.writeInt(v); this}
  def read64(): Long = {_pos += 8; in.readLong()}
  def write64(v: Long) = {_pos += 8; out.writeLong(v); this}
  def readBool(): Boolean = {_pos += 1; in.readByte() > 0}
  def writeBool(v: Boolean) = {_pos += 1; this.write8(if (v) 1 else 0); this}
  def readFloat(): Float = {_pos += 4; in.readFloat()}
  def writeFloat(v: Float) = {_pos += 4; out.writeFloat(v); this}

  def write[T <: BinarySerializable](i: T) = {i.write(this); this}
  def read[T <: BinarySerializable](i: T): T = {i.read(this); i}

  def readBytes(n: Int): Array[Byte] = {
    val res = new Array[Byte](n)
    _pos += n
    in.readFully(res)
    res
  }

  def writeBytes(bytes: Array[Byte]) = {
    out.write(bytes)
    _pos += bytes.length
    this
  }

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

  def readPaddingTo(i: Int): Unit = {
    val endPos = this.pos
    val padding = i - (endPos % i).toInt
    if (padding != i) {
      for (_ <- 1 to padding) this.read8()
    }
  }

  def writePaddingTo(i: Int, v: Byte = 0x00): PrimeDataFile = {
    val endPos = this.pos
    val padding = 32 - (endPos % 32).toInt
    if (padding != 32) {
      for (_ <- 1 to padding) this.write8(v)
    }
    this
  }

  def writePaddingBytesGivenStartOffset(start: Long, count: Int, v: Byte = 0x00): PrimeDataFile= {
    val endPos = this.pos
    val len = endPos - start
    val padding = 32 - (len % 32).toInt
    if (padding != 32) {
      for (_ <- 1 to padding) this.write8(v)
    }
    this
  }

  def pos = _pos
}
