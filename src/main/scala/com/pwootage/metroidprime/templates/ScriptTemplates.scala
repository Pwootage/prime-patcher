package com.pwootage.metroidprime.templates

import scala.xml.{NodeSeq, XML}
import better.files._

object ScriptTemplates {
  private lazy val propertyNames = {
    val in = getClass.getClassLoader.getResourceAsStream("templates/Properties.xml")
    val str = new String(in.bytes.toArray).replaceAll("<\\?xml.*\\?>", "") //Hack off the meta tag... no idea why I have to
    in.close()

    val parsedXml: NodeSeq = XML.loadString(str)
    val pairs = (parsedXml \ "property")
      .map(n => (n \ "@ID").text -> (n \ "@name").text)

    Map(pairs:_*)
  }

  def nameForProperty(id: String): String = {
    propertyNames.getOrElse(id, throw new IllegalArgumentException(s"Unknown ID: $id"))
  }

  def loadTemplate(url: String) = {
    val in = getClass.getClassLoader.getResourceAsStream("templates/" + url)
    val str = new String(in.bytes.toArray).replaceAll("<\\?xml.*\\?>", "") //Hack off the meta tag... no idea why I have to
    in.close()

    val parsedXml: NodeSeq = XML.loadString(str)

    val res = new ScriptTemplate
    res.fromXml(parsedXml)
    res
  }
}
