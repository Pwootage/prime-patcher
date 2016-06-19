package com.pwootage.metroidprime.templates

import com.pwootage.metroidprime.formats.BinarySerializable
import com.pwootage.metroidprime.formats.io.PrimeDataFile

class CharacterScriptPropertyValue extends BinarySerializable {
  var animANCS: Int = 0
  var character: Int = 0
  var defaultAnim: Int = 0

  override def write(f: PrimeDataFile): Unit = {
    f.write32(animANCS)
    f.write32(character)
    f.write32(defaultAnim)
  }

  override def read(f: PrimeDataFile): Unit = {
    animANCS = f.read32()
    character = f.read32()
    defaultAnim = f.read32()
  }
}
