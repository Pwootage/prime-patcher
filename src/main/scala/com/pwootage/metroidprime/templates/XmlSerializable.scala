package com.pwootage.metroidprime.templates

import scala.xml.NodeSeq

trait XmlSerializable {
  def fromXml(v: NodeSeq): Unit
}
