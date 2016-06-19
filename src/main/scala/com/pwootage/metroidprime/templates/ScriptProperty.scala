package com.pwootage.metroidprime.templates

import com.fasterxml.jackson.annotation._

import scala.xml.{Node, NodeSeq}

import XMLUtils._

object ScriptProperty {
  def determineType(n: NodeSeq): ScriptProperty[_] = {
    n(0).label match {
      case "property" =>
        (n \ "@type").text match {
          case ScriptPropertyType.BYTE.identifier => new ByteScriptProperty
          case ScriptPropertyType.SHORT.identifier => new ShortScriptProperty
          case ScriptPropertyType.LONG.identifier => new IntScriptProperty
          case ScriptPropertyType.FLOAT.identifier => new FloatScriptProperty
          case _ =>
            println(n)
            null
        }
    }
  }

}

abstract class ScriptProperty[T] extends XmlSerializable {
  var ID: String = null
  var name: String = null

  var description: Option[String] = None
  var default: Option[T] = None
  var cook_pref: Option[ScriptPropertyCookPreference] = None

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  var versions = Seq[GameVersion]()

  var value: Option[T] = None

  def parseValue(node: NodeSeq): T

  override def fromXml(v: NodeSeq): Unit = {
    ID = (v \ "@ID").text
    name = (v \ "@name").text
    description = (v \ "description").optionalText
    default = (v \ "default").headOption.map(parseValue _)
  }
}

trait NumericScriptProperty[T] extends ScriptProperty[T] {
  var suffix: Option[String] = None

  var min: Option[T] = None
  var max: Option[T] = None

  def range = if (min.isDefined && max.isDefined) Some(s"$min,$max") else None

  def range(x: Option[String]): Unit = x match {
    case Some(v) =>
      val split = v.split(",")
      min = Some(fromString(split(0)))
      max = Some(fromString(split(1)))
    case None => None
  }

  def fromString(v: String): T

  override def parseValue(node: NodeSeq): T = fromString(node.text)

  override def fromXml(v: NodeSeq): Unit = {
    super.fromXml(v)
    range((v \ "range").optionalText)
    suffix = (v \ "suffix").optionalText
  }
}

class ByteScriptProperty extends ScriptProperty[Byte] with NumericScriptProperty[Byte] {
  override def fromString(v: String): Byte = v.toLong.toByte //Make sure we support unsigned
}

class ShortScriptProperty extends ScriptProperty[Short] with NumericScriptProperty[Short] {
  override def fromString(v: String): Short = v.toLong.toShort //Make sure we support unsigned
}

class IntScriptProperty extends ScriptProperty[Int] with NumericScriptProperty[Int] {
  override def fromString(v: String): Int = v.toLong.toInt //Make sure we support unsigned
}

class FloatScriptProperty extends ScriptProperty[Float] with NumericScriptProperty[Float] {
  override def fromString(v: String): Float = v.toFloat
}

class Vector3FScriptProperty extends ScriptProperty[Vector3FScriptPropertyValue] {
  override def parseValue(node: NodeSeq): Vector3FScriptPropertyValue = ???
}

class ColorScriptProperty extends ScriptProperty[ColorScriptPropertyValue] {
  override def parseValue(node: NodeSeq): ColorScriptPropertyValue = ???
}

class FileScriptProperty extends ScriptProperty[Int] {
  var extensions = Seq[String]()

  def extensionsStr = extensions.mkString(",")

  def setSxtensionsStr(v: String): Unit = extensions = v.split(",").map(_.trim)

  override def parseValue(node: NodeSeq): Int = ???
}

class StringScriptProperty extends ScriptProperty[String] {
  override def parseValue(node: NodeSeq): String = ???
}

abstract class TemplatedScriptProperty[T] extends ScriptProperty[T] {
  var template: Option[String] = None

  //TODO: TEMPLATE HANDLING
}

class StructScriptProperty extends TemplatedScriptProperty[ScriptObjectStructValue] {
  var typ: Option[ScriptPropertyStructType] = None

  var properties = Seq[ScriptProperty[_]]()
  default = None

  override def parseValue(node: NodeSeq): ScriptObjectStructValue = ???
}

class EnumScriptProperty extends TemplatedScriptProperty[ScriptObjectEnumValue] {
  var enumerators = Seq[Enumerator]()

  override def parseValue(node: NodeSeq): ScriptObjectEnumValue = ???
}

class BitfieldScriptProperty extends TemplatedScriptProperty[Int] {
  var flags = Seq[Flag]()

  override def parseValue(node: NodeSeq): Int = ???
}

class ArrayScriptProperty extends TemplatedScriptProperty[ScriptObjectArrayValue] {
  var properties = Seq[ScriptProperty[_]]()

  override def parseValue(node: NodeSeq): ScriptObjectArrayValue = ???
}

class GameVersion extends XmlSerializable {
  @JsonProperty("name")
  var name: String = null

  def this(n: String) {
    this()
    this.name = n
  }

  override def fromXml(v: NodeSeq): Unit = {
    name = (v \ "@name").text
  }
}

class Enumerator {
  var ID: String = null
  var name: String = null
}

class Flag {
  var mask: String = null
  var name: String = null
}