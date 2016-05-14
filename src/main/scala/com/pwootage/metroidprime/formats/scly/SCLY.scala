package com.pwootage.metroidprime.formats.scly

import java.io.{ByteArrayInputStream, DataInputStream}

import com.pwootage.metroidprime.formats.BinarySerializable
import com.pwootage.metroidprime.formats.io.PrimeDataFile
import com.pwootage.metroidprime.utils.ByteDiffer

object SCLY {
  val MAGIC = 0x53434C59
}

class SCLY extends BinarySerializable {
  var magic: Int = -1
  var unk1: Int = -1
  var layers = Array[ScriptLayer]()


  override def write(f: PrimeDataFile): Unit = {
    f.write32(magic)
    f.write32(unk1)
    f.write32(layers.length)
    val layerBytes = layers.map(_.toByteArray)
    f.writeArray(layerBytes.map(_.length), _.write32)
    for (layer <- layerBytes) {
      f.writeBytes(layer)
    }
    f.writePaddingTo(32)
  }

  override def read(f: PrimeDataFile): Unit = {
    magic = f.read32()
    if (magic != SCLY.MAGIC) {
      throw new IllegalArgumentException(s"Invalid magic: ${magic.toHexString}")
    }
    unk1 = f.read32()
    val layerCount: Int = f.read32()
    val layerSizes = f.readArrayWithCount(layerCount, _.read32)
    layers = layerSizes.map(f.readBytes(_)).map(bytes => {
      val layer = new ScriptLayer
      layer.read(bytes)
      layer
    })
  }
}
