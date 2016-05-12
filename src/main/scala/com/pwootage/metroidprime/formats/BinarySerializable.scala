package com.pwootage.metroidprime.formats

import java.io.{ByteArrayOutputStream, DataOutputStream}
import java.nio.ByteBuffer

import com.pwootage.metroidprime.formats.io.PrimeDataFile

trait BinarySerializable {
  def write(f: PrimeDataFile): Unit

  def read(f: PrimeDataFile): Unit

  def read(bytes: Array[Byte]): Unit = {
    read(new PrimeDataFile(bytes))
  }

  def toByteArray = {
    val byteOut = new ByteArrayOutputStream()
    this.write(new PrimeDataFile(None, Some(new DataOutputStream(byteOut))))
    byteOut
  }
}