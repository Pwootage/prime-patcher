package com.pwootage.metroidprime.formats.dgrp

import com.pwootage.metroidprime.formats.BinarySerializable
import com.pwootage.metroidprime.formats.io.PrimeDataFile

class DGRP extends BinarySerializable {
  var dependencies = Array[Resource]()

  override def write(f: PrimeDataFile): Unit = {
    f.write32(dependencies.length).writeArray(dependencies)
  }

  override def read(f: PrimeDataFile): Unit = {
    dependencies = f.readArray(f.read32(), () => new Resource)
  }
}
