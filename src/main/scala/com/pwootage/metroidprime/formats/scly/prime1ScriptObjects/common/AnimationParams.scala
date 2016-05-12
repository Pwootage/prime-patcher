package com.pwootage.metroidprime.formats.scly.prime1ScriptObjects.common

import com.pwootage.metroidprime.formats.BinarySerializable
import com.pwootage.metroidprime.formats.io.PrimeDataFile

class AnimationParams extends BinarySerializable {
  var animANCS: Int = -1
  var character: Int = -1
  var defaultAnim: Int = -1
  var unknown1: Int = -1
  var unknown2: Int = -1

  override def write(f: PrimeDataFile): Unit = {
    f.write32(animANCS)
    f.write32(character)
    f.write32(defaultAnim)
    f.write32(unknown1)
    f.write32(unknown2)
  }

  override def read(f: PrimeDataFile): Unit = {
    animANCS = f.read32()
    character = f.read32()
    defaultAnim = f.read32()
    unknown1 = f.read32()
    unknown2 = f.read32()
  }
}
