package com.pwootage.metroidprime.formats.iso

import com.pwootage.metroidprime.formats.BinarySerializable
import com.pwootage.metroidprime.formats.io.PrimeDataFile

class GCIsoHeaders extends BinarySerializable {
  var discHeader = new GCDiscHeader
  var discHeaderInfo = new GCDiscHeaderInfo
  var apploader = new Apploader

  override def write(f: PrimeDataFile): Unit = {
    f.write(discHeader)
    f.write(discHeaderInfo)
    f.write(apploader)
  }

  override def read(f: PrimeDataFile): Unit = {
    f.read(discHeader)
    f.read(discHeaderInfo)
    f.read(apploader)
  }
}
