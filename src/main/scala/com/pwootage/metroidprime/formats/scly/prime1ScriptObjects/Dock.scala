package com.pwootage.metroidprime.formats.scly.prime1ScriptObjects

import com.pwootage.metroidprime.formats.BinarySerializable
import com.pwootage.metroidprime.formats.common.Vec3
import com.pwootage.metroidprime.formats.io.PrimeDataFile

class Dock extends ScriptObjectInstanceBase {
  var active: Boolean = false
  val pos = new Vec3
  val scale = new Vec3
  var dockID: Int = -1
  var areaID: Int = -1
  var autoLoad: Boolean = false

  override def write(f: PrimeDataFile): Unit = {
    super.write(f)
    f.writeBool(active)
    f.write(pos)
    f.write(scale)
    f.write32(dockID)
    f.write32(areaID)
    f.writeBool(autoLoad)
  }

  override def read(f: PrimeDataFile): Unit = {
    super.read(f)
    dockID = f.read32()
    areaID = f.read32()
    autoLoad = f.readBool()
    active = f.readBool()
    f.read(pos)
    f.read(scale)
  }
}
