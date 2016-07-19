package com.pwootage.metroidprime.templates

import java.io.FileNotFoundException

import scala.xml.{NodeSeq, XML}
import better.files._

object ScriptTemplates {
  def loadTemplateXml(templ: String): NodeSeq = {
    val in = ClassLoader.getSystemResourceAsStream("templates/" + templ)
    if (in == null) {
      throw new FileNotFoundException(templ)
    }
    val str = new String(in.bytes.toArray).replaceAll("<\\?xml.*\\?>", "") //Hack off the meta tag... no idea why I have to
    in.close()

    val parsedXml: NodeSeq = XML.loadString(str)
    parsedXml
  }

  private lazy val propertyNames = {
    val parsedXml = loadTemplateXml("Properties.xml")
    val pairs = (parsedXml \ "property")
      .map(n => (n \ "@ID").text -> (n \ "@name").text)

    Map(pairs:_*)
  }

  def nameForProperty(id: String): Option[String] = {
    propertyNames.get(id)
  }

  def loadTemplate(url: String) = {
    val parsedXml = loadTemplateXml(url)

    val res = new ScriptTemplate
    res.fromXml(parsedXml)
    res
  }
}
