package com.pwootage.metroidprime.formats.iso

import com.pwootage.metroidprime.formats.BinarySerializable
import com.pwootage.metroidprime.formats.io.PrimeDataFile

object GCDiscHeader {
  val magic = 0xC2339F3D
}

class GCDiscHeader extends BinarySerializable {
  var gameCode: Int = -1
  var makerCode: Short = -1
  var discID: Byte = -1
  var version: Byte = -1
  var audioStreaming: Byte = -1
  var streamBufferSize: Byte = -1
  var magic: Int = -1
  var name: String = ""
  var debugMonitorOffset: Int = -1
  var debugMonitorAddr: Int = -1
  var bootDolOffset: Int = -1
  var fstOffset: Int = -1
  var fstSize: Int = -1
  var fstMaxSize: Int = -1
  var userPos: Int = -1
  var userLen: Int = -1
  var unknown: Int = -1

  override def write(f: PrimeDataFile): Unit = {
    f.write32(gameCode)
    f.write16(makerCode)
    f.write8(discID)
    f.write8(version)
    f.write8(audioStreaming)
    f.write8(streamBufferSize)
    f.writeBytes(Array.fill(0x12)(0)) //unused (zeros) 0x12 long
    f.write32(magic)
    f.writeString(name)
    f.writePaddingTo(0x0400) //Pos it should end at
    f.write32(debugMonitorAddr)
    f.write32(debugMonitorOffset)
    f.writeBytes(Array.fill(0x18)(0)) //unused (zeros) 0x18 long
    f.write32(bootDolOffset)
    f.write32(fstOffset)
    f.write32(fstSize)
    f.write32(fstMaxSize)
    f.write32(userPos)
    f.write32(userLen)
    f.write32(unknown)
    f.writeBytes(Array.fill(0x04)(0)) //unused (zeros) 0x04 long
  }

  override def read(f: PrimeDataFile): Unit = {
    gameCode = f.read32()
    makerCode = f.read16()
    discID = f.read8()
    version = f.read8()
    audioStreaming = f.read8()
    streamBufferSize = f.read8()
    f.readBytes(0x12) //unused (zeros) 0x12 long
    magic = f.read32()
    name = f.readString()
    f.readPaddingTo(0x0400) //Pos it should end at
    debugMonitorAddr = f.read32()
    debugMonitorOffset = f.read32()
    f.readBytes(0x18) //unused (zeros) 0x18 long
    bootDolOffset = f.read32()
    fstOffset = f.read32()
    fstSize = f.read32()
    fstMaxSize = f.read32()
    userPos = f.read32()
    userLen = f.read32()
    unknown = f.read32()
    f.readBytes(0x04) //unused (zeros) 0x04 long
  }
}
