package com.pwootage.metroidprime.formats.scly.prime1ScriptObjects.common

import com.pwootage.metroidprime.formats.BinarySerializable
import com.pwootage.metroidprime.formats.io.PrimeDataFile

class ActorParams extends BinarySerializable {
  val lightParams = new LightParams
  var scanSCAN: Int = -1
  var xrayModel: Int = -1
  var xraySkin: Int = -1
  var thermalModel: Int = -1
  var thermalSkin: Int = -1
  var unknown1: Boolean = false
  var unknown2: Float = Float.NaN
  var unknown3: Float = Float.NaN
  var unknown4: Int = -1
  val visorParams = new VisorParams
  var enableThermalHeat: Boolean = false
  var unknown5: Boolean = false
  var unknown6: Boolean = false
  var unknown7: Float = Float.NaN

  override def write(f: PrimeDataFile): Unit = {
    f.write(lightParams)
    f.write32(scanSCAN)
    f.write32(xrayModel)
    f.write32(xraySkin)
    f.write32(thermalModel)
    f.write32(thermalSkin)
    f.writeBool(unknown1)
    f.writeFloat(unknown2)
    f.writeFloat(unknown3)
    f.write32(unknown4)
    f.write(visorParams)
    f.writeBool(enableThermalHeat)
    f.writeBool(unknown5)
    f.writeBool(unknown6)
    f.writeFloat(unknown7)
  }

  override def read(f: PrimeDataFile): Unit = {
    f.read(lightParams)
    scanSCAN = f.read32()
    xrayModel = f.read32()
    xraySkin = f.read32()
    thermalModel = f.read32()
    thermalSkin = f.read32()
    unknown1 = f.readBool()
    unknown2 = f.readFloat()
    unknown3 = f.readFloat()
    unknown4 = f.read32()
    f.read(visorParams)
    enableThermalHeat = f.readBool()
    unknown5 = f.readBool()
    unknown6 = f.readBool()
    unknown7 = f.readFloat()
  }
}
