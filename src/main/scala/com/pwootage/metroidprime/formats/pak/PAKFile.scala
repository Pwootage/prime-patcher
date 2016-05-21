package com.pwootage.metroidprime.formats.pak

import com.pwootage.metroidprime.formats.BinarySerializable
import com.pwootage.metroidprime.formats.common.PrimeVersion
import com.pwootage.metroidprime.formats.io.PrimeDataFile
import com.pwootage.metroidprime.utils.{DataTypeConversion, Logger}

object PAKFile {
  val Version = 0x00030005
}

class PAKFile(primeVersion: PrimeVersion) extends BinarySerializable {
  var version: Int = -1
  var unused: Int = -1

  var namedResources = Array[NamedResource]()
  var resources = Array[Resource]()

  override def write(f: PrimeDataFile): Unit = {
    f.write32(version)
    f.write32(unused)
    f.write32(namedResources.length).writeArray(namedResources)
    f.write32(resources.length).writeArray(resources)
  }

  override def read(f: PrimeDataFile): Unit = {
    version = f.read32()
    if (version != PAKFile.Version) {
      throw new IllegalArgumentException(s"Invalid version number in PAK: $version")
    }
    unused = f.read32()

    namedResources = f.readArray(f.read32(), () => new NamedResource)
    resources = f.readArray(f.read32(), () => new Resource)
  }

  def toBasicResourceList = BasicResourceList(
    namedResources = Map[String, String](namedResources.map(r => r.name -> r.idStr):_*),
    resources = resources.map(_.idStr)
  )

  def fromBasicResourceList(res: BasicResourceList): Unit = {
    namedResources = res.namedResources.map(tuple => {
      val (name, idStr) = tuple
      val (id, typ) = DataTypeConversion.strResourceToIdAndType(idStr)
      val res = new NamedResource
      res.name = name
      res.id = id
      res.typ = typ
      res
    }).toArray
    resources = res.resources.map(r => {
      val (id, typ) = DataTypeConversion.strResourceToIdAndType(r)
      val res = new Resource
      res.id = id
      res.typ = typ
      res
    }).toArray
  }
}
