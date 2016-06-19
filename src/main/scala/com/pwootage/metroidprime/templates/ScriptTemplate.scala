package com.pwootage.metroidprime.templates

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.pwootage.metroidprime.templates.XMLUtils._

import scala.xml.NodeSeq


class ScriptTemplate extends XmlSerializable {
  var name: String = null
  var versions = Seq[GameVersion]()
  var properties = Seq[ScriptProperty[_]]()
  //var editor = Seq[]

  override def fromXml(v: NodeSeq): Unit = {
    name = (v \ "name").text
    versions = (v \ "versions" \ "version").array(() => new GameVersion)
    properties = (v \ "properties" \ "property").array(n => ScriptProperty.determineType(n))
  }
}
