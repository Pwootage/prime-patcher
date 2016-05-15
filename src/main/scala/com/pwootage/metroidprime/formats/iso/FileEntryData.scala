package com.pwootage.metroidprime.formats.iso

import com.pwootage.metroidprime.formats.BinarySerializable
import com.pwootage.metroidprime.formats.io.PrimeDataFile

class FileEntryData extends BinarySerializable {
  var isDirectory = false
  var filenameOffset: Int = -1
  var fileOffset: Int = -1
  var fileLength: Int = -1

  def parentOffset = fileOffset
  def numEntries = fileLength
  def nextOffset = fileLength

  override def write(f: PrimeDataFile): Unit = {
    val flags = if (isDirectory) 1 else 0
    val flagsAndOffset = (filenameOffset & 0xFFFFFF) | (flags << 24)
    f.write32(flagsAndOffset)
    f.write32(fileOffset)
    f.write32(fileLength)
  }

  override def read(f: PrimeDataFile): Unit = {
    val flagsAndOffset = f.read32()
    isDirectory = (flagsAndOffset >> 24) > 0
    filenameOffset = flagsAndOffset & 0xFFFFFF
    fileOffset = f.read32()
    fileLength = f.read32()
  }
}
