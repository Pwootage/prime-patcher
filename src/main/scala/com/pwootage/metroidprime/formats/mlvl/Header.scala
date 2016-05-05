package com.pwootage.metroidprime.formats.mlvl

import com.pwootage.metroidprime.formats.BinarySerializable
import java.nio.ByteBuffer

import com.pwootage.metroidprime.formats.io.PrimeDataFile

object Header {
  val MAGIC = 0xDEAFBABE
  val VERSION_PRIME_1 = 0x11
  val VERSION_PRIME_2 = 0x17
}

class Header extends BinarySerializable {
  import PrimeDataFile.Types._
  var magic = u32
  var version = u32
  var worldNameSTRG = u32
  var darkWorldNameSTRG = u32
  var unknown1 = u32
  var worldSAVW = u32
  var defaultSkyboxCDML = u32

  def prime1: Boolean = version == Header.VERSION_PRIME_1
  def prime2: Boolean = version == Header.VERSION_PRIME_2

  def write(f: PrimeDataFile) {
    if (magic != Header.MAGIC) {
      throw new IllegalArgumentException("Invalid magic: " + magic)
    }
    if (!(version == Header.VERSION_PRIME_1 || prime2)) {
      throw new IllegalArgumentException("Invalid version: " + version)
    }
    f.i32(magic)
    f.i32(version)
    f.i32(worldNameSTRG)
    if (prime2) {
      f.i32(darkWorldNameSTRG)
      f.i32(unknown1)
    }
    f.i32(worldSAVW)
    f.i32(defaultSkyboxCDML)
  }

  def read(buff: PrimeDataFile) {
    magic = buff.i32()
    version = buff.i32()
    worldNameSTRG = buff.i32()
    if (prime2) {
      darkWorldNameSTRG = buff.i32()
      unknown1 = buff.i32()
    }
    worldSAVW = buff.i32()
    defaultSkyboxCDML = buff.i32()
    if (magic != Header.MAGIC) {
      throw new IllegalArgumentException("Invalid magic: " + magic)
    }
    if (!(version == Header.VERSION_PRIME_1 || prime2)) {
      throw new IllegalArgumentException("Invalid version: " + version)
    }
  }
}