package com.pwootage.metroidprime.templates

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.pwootage.metroidprime.formats.BinarySerializable
import com.pwootage.metroidprime.formats.io.PrimeDataFile
import com.pwootage.metroidprime.templates.XMLUtils._

import scala.xml.NodeSeq


class ScriptTemplate extends XmlSerializable with BinarySerializable {
  var name: String = null
  var versions = Seq[GameVersion]()
  var properties = Seq[ScriptProperty[_]]()
  //var editor = Seq[]

  override def fromXml(v: NodeSeq): Unit = {
    name = (v \ "name").text
    versions = (v \ "versions" \ "version").array(() => new GameVersion)
    properties = (v \ "properties" \ "_").array(n => ScriptProperty.determineType(n))
  }


  override def toString = s"ScriptTemplate($name, ${properties.length})"

  override def write(f: PrimeDataFile): Unit = {
    for (prop <- properties) {
      prop.write(f)
    }
  }

  override def read(f: PrimeDataFile): Unit = {
    for (prop <- properties) {
      prop.read(f)
    }
  }

}
