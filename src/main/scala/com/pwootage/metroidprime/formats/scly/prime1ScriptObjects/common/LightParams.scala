package com.pwootage.metroidprime.formats.scly.prime1ScriptObjects.common

import com.pwootage.metroidprime.formats.BinarySerializable
import com.pwootage.metroidprime.formats.common.{RGBA, Vec3}
import com.pwootage.metroidprime.formats.io.PrimeDataFile

class LightParams extends BinarySerializable {
  var unknown1: Boolean = false
  var unknown2: Float = Float.NaN
  var shadowTessellation: Int = 0
  var unknown3: Float = Float.NaN
  var unknown4: Float = Float.NaN
  val unknown5 = new RGBA
  var unknown6: Boolean = false
  var worldLightingOption: Int = -1
  var lightRecalculation: Int = -1
  val unknown7 = new Vec3
  var unknown8: Int = -1
  var unknown9: Int = -1
  var unknown10: Boolean = false
  var lightLayerIndex: Int = -1
  var unknown11: Int = -1

  override def write(f: PrimeDataFile): Unit = {
    f.writeBool(unknown1)
    f.writeFloat(unknown2)
    f.write32(shadowTessellation)
    f.writeFloat(unknown3)
    f.writeFloat(unknown4)
    f.write(unknown5)
    f.writeBool(unknown6)
    f.write32(worldLightingOption)
    f.write32(lightRecalculation)
    f.write(unknown7)
    f.write32(unknown8)
    f.write32(unknown9)
    f.writeBool(unknown10)
    f.write32(lightLayerIndex)
    f.write32(unknown11)
  }

  override def read(f: PrimeDataFile): Unit = {
    unknown1 = f.readBool()
    unknown2 = f.readFloat()
    shadowTessellation = f.read32()
    unknown3 = f.readFloat()
    unknown4 = f.readFloat()
    f.read(unknown5)
    unknown6 = f.readBool()
    worldLightingOption = f.read32()
    lightRecalculation = f.read32()
    f.read(unknown7)
    unknown8 = f.read32()
    unknown9 = f.read32()
    unknown10 = f.readBool()
    lightLayerIndex = f.read32()
    unknown11 = f.read32()
  }
}
