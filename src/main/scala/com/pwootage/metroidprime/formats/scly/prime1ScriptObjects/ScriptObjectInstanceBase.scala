package com.pwootage.metroidprime.formats.scly.prime1ScriptObjects

import com.pwootage.metroidprime.formats.BinarySerializable
import com.pwootage.metroidprime.formats.common.Vec3
import com.pwootage.metroidprime.formats.io.PrimeDataFile

class ScriptObjectInstanceBase extends BinarySerializable {
  var name = ""

  override def write(f: PrimeDataFile): Unit = {
    f.writeString(name)
  }

  override def read(f: PrimeDataFile): Unit = {
    name = f.readString()
  }

  override def toString = s"ScriptObjectInstanceBase($name)"
}
