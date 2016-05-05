package com.pwootage.metroidprime.formats

import java.nio.ByteBuffer

import com.pwootage.metroidprime.formats.io.PrimeDataFile

trait BinarySerializable {
  def write(f: PrimeDataFile): Unit

  def read(f: PrimeDataFile): Unit
}