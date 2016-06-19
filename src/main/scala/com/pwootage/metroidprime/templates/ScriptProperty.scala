package com.pwootage.metroidprime.templates

import com.fasterxml.jackson.annotation._

import scala.xml.{Node, NodeSeq}
import XMLUtils._
import com.pwootage.metroidprime.utils.DataTypeConversion

object ScriptProperty {
  def determineType(n: NodeSeq): ScriptProperty[_] = {
    n(0).label match {
      case "property" =>
        (n \ "@type").text match {
          case ScriptPropertyType.BOOL.identifier => new BooleanScriptProperty
          case ScriptPropertyType.BYTE.identifier => new ByteScriptProperty
          case ScriptPropertyType.SHORT.identifier => new ShortScriptProperty
          case ScriptPropertyType.LONG.identifier => new IntScriptProperty
          case ScriptPropertyType.FLOAT.identifier => new FloatScriptProperty
          case ScriptPropertyType.STRING.identifier => new StringScriptProperty
          case ScriptPropertyType.VECTOR3F.identifier => new Vector3FScriptProperty
          case ScriptPropertyType.COLOR.identifier => new ColorScriptProperty
          case ScriptPropertyType.FILE.identifier => new FileScriptProperty
          case ScriptPropertyType.STRING.identifier => new StringScriptProperty
          case "" => new StringScriptProperty
          case null => new StringScriptProperty
          case _ =>
            println(n)
            null
        }
      case "struct" => new StructScriptProperty
      case "enum" => new EnumScriptProperty
      case "array" => new ArrayScriptProperty
      case "bitfield" => new BitfieldScriptProperty
      case _ => null
    }
  }

}

abstract class ScriptProperty[T] extends XmlSerializable {
  var ID: String = null
  var name: String = null
  var description: Option[String] = None
  var default: Option[T] = None
  var cook_pref = ScriptPropertyCookPreference.NOPREF
  var versions = Seq[GameVersion]()
  var value: Option[T] = None

  def parseValue(node: NodeSeq): T

  def defaultDefault: Option[T] = None

  override def fromXml(v: NodeSeq): Unit = {
    ID = (v \ "@ID").text
    name = (v \ "@name").headOption.map(_.text)
      .orElse(ScriptTemplates.nameForProperty(ID)).getOrElse(name)
    description = (v \ "description").optionalText
    default = (v \ "default").headOption
      .map(parseValue _)
      .orElse(defaultDefault)
    cook_pref = (v \ "cook_pref").headOption.map(_.text)
      .map(ScriptPropertyCookPreference.fromIdentifier)
      .getOrElse(ScriptPropertyCookPreference.NOPREF)
    versions = (v \ "versions" \ "version").array(() => new GameVersion())
  }


  override def toString = s"${getClass.getSimpleName}($ID, $name, $description)"
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

  override def defaultDefault: Option[T] = Some(fromString("0"))

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
  override def parseValue(node: NodeSeq): Vector3FScriptPropertyValue = new Vector3FScriptPropertyValue(node.text)

  override def defaultDefault: Option[Vector3FScriptPropertyValue] = Some(new Vector3FScriptPropertyValue("0,0,0"))
}

class ColorScriptProperty extends ScriptProperty[ColorScriptPropertyValue] {
  override def parseValue(node: NodeSeq): ColorScriptPropertyValue = new ColorScriptPropertyValue(node.text)

  override def defaultDefault: Option[ColorScriptPropertyValue] = Some(new ColorScriptPropertyValue("1,1,1,1"))
}

class FileScriptProperty extends ScriptProperty[Int] {
  var extensions = Seq[String]()

  def extensionsStr = extensions.mkString(",")

  def setSxtensionsStr(v: String): Unit = extensions = v.split(",").map(_.trim)

  override def parseValue(node: NodeSeq): Int = node.text.toInt

  override def defaultDefault: Option[Int] = Some(0xFFFFFFFF)
}

class StringScriptProperty extends ScriptProperty[String] {
  override def parseValue(node: NodeSeq): String = node.text

  override def defaultDefault: Option[String] = Some("")
}

class BooleanScriptProperty extends ScriptProperty[Boolean] {
  override def parseValue(node: NodeSeq): Boolean = node.text.toBoolean

  override def defaultDefault: Option[Boolean] = Some(false)
}

abstract class TemplatedScriptProperty[T] extends ScriptProperty[T] {
  var template: Option[String] = None

  override def fromXml(v: NodeSeq): Unit = {
    (v \ "@template").headOption.map(_.text).foreach(templ => {
      template = Some(templ)
      val xml = ScriptTemplates.loadTemplateXml(templ)
      fromXml(xml)
    })
    super.fromXml(v)
  }
}

class StructScriptProperty extends TemplatedScriptProperty[ScriptObjectStructValue] {
  var typ: ScriptPropertyStructType = ScriptPropertyStructType.SINGLE

  var properties = Map[String, ScriptProperty[_]]()
  default = None

  override def parseValue(node: NodeSeq): ScriptObjectStructValue = ???
  override def fromXml(v: NodeSeq): Unit = {
    super.fromXml(v)

    typ = (v \ "@type").headOption.map(_.text)
      .map(ScriptPropertyStructType.fromIdentifier)
      .getOrElse(typ)

    (v \ "properties" \ "_").foreach(prop => {
      val id = (prop \ "@ID").text.trim
      if (properties.contains(id)) {
        properties(id).fromXml(prop)
      } else {
        val parsed = prop.as(ScriptProperty.determineType _)
        properties += parsed.ID -> parsed
      }
      //Merge into existing property
    })
  }
}

class EnumScriptProperty extends TemplatedScriptProperty[Int] {
  var enumerators = Seq[Enumerator]()

  override def parseValue(node: NodeSeq): Int = DataTypeConversion.stringToLong(node.text).toInt
}

class BitfieldScriptProperty extends TemplatedScriptProperty[Int] {
  var flags = Seq[Flag]()

  override def parseValue(node: NodeSeq): Int = DataTypeConversion.stringToLong(node.text).toInt

  override def defaultDefault: Option[Int] = Some(0)
}

class ArrayScriptProperty extends TemplatedScriptProperty[ScriptObjectArrayValue] {
  var properties = Seq[ScriptProperty[_]]()

  //Not sure how to do defaults here....
  //Also, no templated arrays are ever used, but they *should* work

  override def parseValue(node: NodeSeq): ScriptObjectArrayValue = new ScriptObjectArrayValue
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

  override def toString = s"GameVersion($name)"
}

class Enumerator {
  var ID: String = null
  var name: String = null
}

class Flag {
  var mask: String = null
  var name: String = null
}