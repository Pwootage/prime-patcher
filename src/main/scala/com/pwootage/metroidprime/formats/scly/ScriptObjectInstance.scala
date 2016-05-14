package com.pwootage.metroidprime.formats.scly

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, DataInputStream, DataOutputStream}

import com.pwootage.metroidprime.formats.BinarySerializable
import com.pwootage.metroidprime.formats.io.PrimeDataFile
import com.pwootage.metroidprime.formats.scly.prime1ScriptObjects.ScriptObjectInstanceBase

class ScriptObjectInstance extends BinarySerializable {
  var typ: Byte = -1
  var id: Int = -1
  var connections = Array[ScriptObjectConnection]()
  var propertyCount: Int = -1
  var binaryData = Array[Byte]()

  override def write(f: PrimeDataFile): Unit = {
    f.write8(typ)

    val bodyOut = {
      val bos = new ByteArrayOutputStream()
      val pdf = new PrimeDataFile(new DataOutputStream(bos))
      pdf.write32(id)
      pdf.write32(connections.length).writeArray(connections)
      pdf.write32(propertyCount)
      pdf.writeBytes(binaryData)
      bos.toByteArray
    }
    f.write32(bodyOut.length)
    f.writeBytes(bodyOut)
  }

  override def read(f: PrimeDataFile): Unit = {
    typ = f.read8()
    val size = f.read32()

    val objectBody = f.readBytes(size)
    val body = new PrimeDataFile(objectBody)

    id = body.read32()
    connections = body.readArray(body.read32(), () => new ScriptObjectConnection)
    propertyCount = body.read32()
    binaryData = objectBody.slice(body.pos.toInt, objectBody.length)
  }

  def pdfForBody = new PrimeDataFile(binaryData)

  def typeEnum = Prime1ScriptObjectType.fromID(typ)
  def typeString = typeEnum.toString

  def name = try {
    val obj = new ScriptObjectInstanceBase
    obj.read(pdfForBody)
    obj.name
  } catch {
    case _: Throwable => "<ERR>"
  }

  override def toString: String = s"ScriptObjectInstance($typeString, $id, $name, connectionsCount=${connections.length}, binaryLength=${binaryData.length})"
}
