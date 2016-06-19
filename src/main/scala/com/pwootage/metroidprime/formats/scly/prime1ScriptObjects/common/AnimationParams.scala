package com.pwootage.metroidprime.formats.scly.prime1ScriptObjects.common

import com.pwootage.metroidprime.formats.BinarySerializable
import com.pwootage.metroidprime.formats.io.PrimeDataFile

class AnimationParams extends BinarySerializable {
  var propertyCount: Int = -1
  var animANCS: Int = -1
  var character: Int = -1
  var defaultAnim: Int = -1

  override def write(f: PrimeDataFile): Unit = {
    f.write32(propertyCount)
    f.write32(animANCS)
    f.write32(character)
    f.write32(defaultAnim)
  }

  override def read(f: PrimeDataFile): Unit = {
    propertyCount = f.read32()
    animANCS = f.read32()
    character = f.read32()
    defaultAnim = f.read32()
  }
}
