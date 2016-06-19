package com.pwootage.metroidprime.formats.mlvl

import com.pwootage.metroidprime.formats.BinarySerializable
import java.nio.ByteBuffer

import com.pwootage.metroidprime.formats.common.PrimeVersion
import com.pwootage.metroidprime.formats.io.PrimeDataFile

object Header {
  val MAGIC = 0xDEAFBABE
  val VERSION_PRIME_1 = 0x11
  val VERSION_PRIME_2 = 0x17
}

class Header extends BinarySerializable {
  var magic: Int = -1
  var version: Int = -1
  var worldNameSTRG: Int = -1
  var darkWorldNameSTRG: Int = -1
  var unknown1: Int = -1
  var worldSAVW: Int = -1
  var defaultSkyboxCMDL: Int = -1

  def primeVersion = if (prime1) {
    PrimeVersion.PRIME_1
  } else if (prime2) {
    PrimeVersion.PRIME_2
  } else ???

  def prime1: Boolean = version == Header.VERSION_PRIME_1
  def prime2: Boolean = version == Header.VERSION_PRIME_2

  def write(f: PrimeDataFile) {
    if (magic != Header.MAGIC) {
      throw new IllegalArgumentException("Invalid magic: " + magic)
    }
    if (!(version == Header.VERSION_PRIME_1 || prime2)) {
      throw new IllegalArgumentException("Invalid version: " + version)
    }
    f.write32(magic)
    f.write32(version)
    f.write32(worldNameSTRG)
    if (prime2) {
      f.write32(darkWorldNameSTRG)
      f.write32(unknown1)
    }
    f.write32(worldSAVW)
    f.write32(defaultSkyboxCMDL)
  }

  def read(buff: PrimeDataFile) {
    magic = buff.read32()
    version = buff.read32()
    worldNameSTRG = buff.read32()
    if (prime2) {
      darkWorldNameSTRG = buff.read32()
      unknown1 = buff.read32()
    }
    worldSAVW = buff.read32()
    defaultSkyboxCMDL = buff.read32()
    if (magic != Header.MAGIC) {
      throw new IllegalArgumentException("Invalid magic: " + magic)
    }
    if (!(version == Header.VERSION_PRIME_1 || prime2)) {
      throw new IllegalArgumentException("Invalid version: " + version)
    }
  }
}