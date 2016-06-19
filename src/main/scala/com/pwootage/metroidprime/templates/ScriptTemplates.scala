package com.pwootage.metroidprime.templates

import scala.xml.{NodeSeq, XML}
import better.files._

object ScriptTemplates {
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
