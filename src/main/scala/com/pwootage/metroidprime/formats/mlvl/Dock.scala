package com.pwootage.metroidprime.formats.mlvl

import com.pwootage.metroidprime.formats.BinarySerializable
import com.pwootage.metroidprime.formats.common.Vec3
import com.pwootage.metroidprime.formats.io.PrimeDataFile

class Dock extends BinarySerializable {
  var connectingDocks = Array[ConnectingDock]()
  var coordinates = Array[Vec3]()

  override def write(f: PrimeDataFile): Unit = {
    f.write32(connectingDocks.length).writeArray(connectingDocks)
    f.write32(coordinates.length).writeArray(coordinates)
  }

  override def read(f: PrimeDataFile): Unit = {
    connectingDocks = f.readArray(f.read32(), () => new ConnectingDock)
    coordinates = f.readArray(f.read32(), () => new Vec3)
  }
}
