package com.pwootage.metroidprime.formats.iso

import com.pwootage.metroidprime.formats.BinarySerializable
import com.pwootage.metroidprime.formats.io.PrimeDataFile

class GCDiscHeaderInfo extends BinarySerializable {
  var debugMonitorSize: Int = -1
  var simulatedMemorySize: Int = -1
  var argOffset: Int = -1
  var debugFlag: Int = -1
  var trackLocation: Int = -1
  var trackSize: Int = -1
  var countryCode: Int = -1
  var unk: Int = -1

  override def write(f: PrimeDataFile): Unit = {
    f.write32(debugMonitorSize)
    f.write32(simulatedMemorySize)
    f.write32(debugFlag)
    f.write32(trackLocation)
    f.write32(trackSize)
    f.write32(countryCode)
    f.write32(unk)
    f.writePaddingTo(0x2440)
  }

  override def read(f: PrimeDataFile): Unit = {
    debugMonitorSize = f.read32()
    simulatedMemorySize = f.read32()
    debugFlag = f.read32()
    trackLocation = f.read32()
    trackSize = f.read32()
    countryCode = f.read32()
    unk = f.read32()
    f.readPaddingTo(0x2440)
  }
}
