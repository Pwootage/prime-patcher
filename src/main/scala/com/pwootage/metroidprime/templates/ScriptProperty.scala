package com.pwootage.metroidprime.templates


import com.fasterxml.jackson.annotation._
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node._
import com.pwootage.metroidprime.formats.BinarySerializable
import com.pwootage.metroidprime.formats.io.PrimeDataFile
import com.pwootage.metroidprime.templates.XMLUtils._
import com.pwootage.metroidprime.utils.{DataTypeConversion, PrimeJacksonMapper}
import jdk.nashorn.internal.runtime.regexp.joni.ast.StringNode

import scala.collection.JavaConversions._
import scala.collection.mutable
import scala.xml.NodeSeq

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
          case ScriptPropertyType.CHARACTER.identifier => new CharacterScriptProperty
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

abstract class ScriptProperty[T] extends XmlSerializable with BinarySerializable with Cloneable {
  var ID: String = null
  var name: String = null
  var description: Option[String] = None
  var default: Option[T] = None
  var cookPref = ScriptPropertyCookPreference.NOPREF
  var versions = Seq[GameVersion]()
  var value: Option[T] = None

  def parseValue(node: NodeSeq): T

  def defaultDefault: Option[T] = None

  def applyPatch(v: JsonNode): Unit

  def valueAsJson: JsonNode

  override def fromXml(v: NodeSeq): Unit = {
    ID = (v \ "@ID").text
    name = (v \ "@name").headOption.map(_.text)
      .orElse(ScriptTemplates.nameForProperty(ID)).getOrElse(name)
    description = (v \ "description").optionalText
    default = (v \ "default").headOption
      .map(parseValue _)
      .orElse(defaultDefault)
    cookPref = (v \ "cook_pref").headOption.map(_.text)
      .map(ScriptPropertyCookPreference.fromIdentifier)
      .getOrElse(ScriptPropertyCookPreference.NOPREF)
    versions = (v \ "versions" \ "version").array(() => new GameVersion())
  }

  def shouldCook = cookPref != ScriptPropertyCookPreference.NEVER

  override def toString = s"${getClass.getSimpleName}($ID, $name, $description)"

  def valueOrDefaultValue: T = {
    value.orElse(defaultDefault).get
  }

  def copy(): ScriptProperty[T] = clone().asInstanceOf[ScriptProperty[T]]
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

  override def applyPatch(v: JsonNode): Unit = value = Some(fromString(v.asText()))

  override def parseValue(node: NodeSeq): T = fromString(node.text)

  override def fromXml(v: NodeSeq): Unit = {
    super.fromXml(v)
    range((v \ "range").optionalText)
    suffix = (v \ "suffix").optionalText
  }
}

class ByteScriptProperty extends ScriptProperty[Byte] with NumericScriptProperty[Byte] {
  override def fromString(v: String): Byte = DataTypeConversion.stringToLong(v).toByte //Make sure we support unsigned

  override def write(f: PrimeDataFile): Unit = {
    if (shouldCook) f.write8(valueOrDefaultValue)
  }

  override def read(f: PrimeDataFile): Unit = {
    if (shouldCook) value = Some(f.read8())
  }

  override def valueAsJson: JsonNode = TextNode.valueOf("0x" + (valueOrDefaultValue & 0xFFL).toHexString)
}

class ShortScriptProperty extends ScriptProperty[Short] with NumericScriptProperty[Short] {
  override def fromString(v: String): Short = DataTypeConversion.stringToLong(v).toShort //Make sure we support unsigned

  override def write(f: PrimeDataFile): Unit = {
    if (shouldCook) f.write16(valueOrDefaultValue)
  }

  override def read(f: PrimeDataFile): Unit = {
    if (shouldCook) value = Some(f.read16())
  }

  override def valueAsJson: JsonNode = TextNode.valueOf("0x" + (valueOrDefaultValue & 0xFFFFL).toHexString)
}

class IntScriptProperty extends ScriptProperty[Int] with NumericScriptProperty[Int] {
  override def fromString(v: String): Int = DataTypeConversion.stringToLong(v).toInt //Make sure we support unsigned

  override def write(f: PrimeDataFile): Unit = {
    if (shouldCook) f.write32(valueOrDefaultValue)
  }

  override def read(f: PrimeDataFile): Unit = {
    if (shouldCook) value = Some(f.read32())
  }

  override def valueAsJson: JsonNode = TextNode.valueOf("0x" + (valueOrDefaultValue & 0xFFFFFFFFL).toHexString)
}

class CharacterScriptProperty extends ScriptProperty[CharacterScriptPropertyValue] {
  override def parseValue(node: NodeSeq): CharacterScriptPropertyValue = ???

  override def defaultDefault: Option[CharacterScriptPropertyValue] = Some(new CharacterScriptPropertyValue)

  override def write(f: PrimeDataFile): Unit = {
    if (shouldCook) valueOrDefaultValue.write(f)
  }

  override def read(f: PrimeDataFile): Unit = {
    if (shouldCook) {
      value = Some(new CharacterScriptPropertyValue)
      valueOrDefaultValue.read(f)
    }
  }

  override def applyPatch(v: JsonNode): Unit = {
    if (value.isEmpty) value = defaultDefault

    if (v.has("animANCS")) {
      val ancs = v.get("animANCS").asText
      if (ancs.contains('.')) {
        if (!ancs.endsWith(".ANCS")) throw new IllegalArgumentException(s"Must provide ANCS to anim (got $ancs)")
        val (id, typ) = DataTypeConversion.strResourceToIdAndType(ancs)
        value.get.animANCS = id
      } else {
        value.get.animANCS = DataTypeConversion.stringToLong(ancs).toInt
      }
    }

    if (v.has("character")) {
      value.get.character = DataTypeConversion.stringToLong(v.get("character").asText).toInt
    }

    if (v.has("defaultAnim")) {
      value.get.defaultAnim = DataTypeConversion.stringToLong(v.get("defaultAnim").asText).toInt
    }
  }

  override def valueAsJson: JsonNode = {
    val v = valueOrDefaultValue
    val node = PrimeJacksonMapper.mapper.createObjectNode()
    node.set("animANCS", TextNode.valueOf("0x" + (v.animANCS & 0xFFFFFFFFL).toHexString))
    node.set("character", TextNode.valueOf("0x" + (v.character & 0xFFFFFFFFL).toHexString))
    node.set("defaultAnim", TextNode.valueOf("0x" + (v.defaultAnim & 0xFFFFFFFFL).toHexString))
    node
  }
}

class FloatScriptProperty extends ScriptProperty[Float] with NumericScriptProperty[Float] {
  override def fromString(v: String): Float = v.toFloat

  override def write(f: PrimeDataFile): Unit = {
    if (shouldCook) f.writeFloat(valueOrDefaultValue)
  }
  override def read(f: PrimeDataFile): Unit = {
    if (shouldCook) value = Some(f.readFloat())
  }

  override def valueAsJson: JsonNode = FloatNode.valueOf(valueOrDefaultValue)
}

class Vector3FScriptProperty extends ScriptProperty[Vector3FScriptPropertyValue] {
  override def parseValue(node: NodeSeq): Vector3FScriptPropertyValue = new Vector3FScriptPropertyValue(node.text)

  override def defaultDefault: Option[Vector3FScriptPropertyValue] = Some(new Vector3FScriptPropertyValue("0,0,0"))

  override def write(f: PrimeDataFile): Unit = {
    if (shouldCook) valueOrDefaultValue.write(f)
  }

  override def read(f: PrimeDataFile): Unit = {
    if (shouldCook) {
      value = Some(new Vector3FScriptPropertyValue)
      value.get.read(f)
    }
  }

  override def applyPatch(v: JsonNode): Unit = {
    if (value.isEmpty) value = defaultDefault
    if (v.has("x")) {
      value.get.x = v.get("x").asText.toFloat
    }
    if (v.has("y")) {
      value.get.y = v.get("y").asText.toFloat
    }
    if (v.has("z")) {
      value.get.z = v.get("z").asText.toFloat
    }
  }

  override def valueAsJson: JsonNode = {
    val v = valueOrDefaultValue
    PrimeJacksonMapper.mapper.createObjectNode()
      .put("x", v.x)
      .put("y", v.y)
      .put("z", v.z)
  }
}

class ColorScriptProperty extends ScriptProperty[ColorScriptPropertyValue] {
  override def parseValue(node: NodeSeq): ColorScriptPropertyValue = new ColorScriptPropertyValue(node.text)

  override def defaultDefault: Option[ColorScriptPropertyValue] = Some(new ColorScriptPropertyValue("1,1,1,1"))

  override def write(f: PrimeDataFile): Unit = {
    if (shouldCook) valueOrDefaultValue.write(f)
  }

  override def read(f: PrimeDataFile): Unit = {
    if (shouldCook) {
      value = Some(new ColorScriptPropertyValue)
      value.get.read(f)
    }
  }

  override def applyPatch(v: JsonNode): Unit = {
    if (value.isEmpty) value = defaultDefault
    if (v.has("r")) {
      value.get.r = v.get("r").asText.toFloat
    }
    if (v.has("g")) {
      value.get.g = v.get("g").asText.toFloat
    }
    if (v.has("b")) {
      value.get.b = v.get("b").asText.toFloat
    }
    if (v.has("a")) {
      value.get.b = v.get("a").asText.toFloat
    }
  }

  override def valueAsJson: JsonNode = {
    val v = valueOrDefaultValue
    PrimeJacksonMapper.mapper.createObjectNode()
      .put("r", v.r)
      .put("g", v.g)
      .put("b", v.b)
      .put("a", v.a)
  }
}

class FileScriptProperty extends ScriptProperty[Int] {
  var extensions = Seq[String]()

  def extensionsStr = extensions.mkString(",")

  def setExtensionsStr(v: String): Unit = extensions = v.split(",").map(_.trim)

  override def parseValue(node: NodeSeq): Int = node.text.toInt

  override def defaultDefault: Option[Int] = Some(-1)

  override def fromXml(v: NodeSeq): Unit = {
    super.fromXml(v)
    (v \ "@extensions").headOption.map(_.text).foreach(setExtensionsStr _)
  }

  override def write(f: PrimeDataFile): Unit = {
    if (shouldCook) f.write32(valueOrDefaultValue)
  }
  override def read(f: PrimeDataFile): Unit = {
    if (shouldCook) value = Some(f.read32())
  }
  override def applyPatch(v: JsonNode): Unit = {
    val file = v.asText
    if (file.contains('.')) {
      val (id, typ) = DataTypeConversion.strResourceToIdAndType(file)
      val typString = DataTypeConversion.intContainingCharsAsStr(typ)
      if (!extensions.contains(typString)) {
        throw new IllegalArgumentException(s"Format not allowed (allowed ${extensions.mkString(",")}, got $file)")
      }
      value = Some(id)
    } else {
      value = Some(DataTypeConversion.stringToLong(file).toInt)
    }
  }

  //TODO: multiple extensions?
  override def valueAsJson: JsonNode = TextNode.valueOf("0x" + (valueOrDefaultValue & 0xFFFFFFFFL).toHexString)
}

class StringScriptProperty extends ScriptProperty[String] {
  override def parseValue(node: NodeSeq): String = node.text

  override def defaultDefault: Option[String] = Some("")

  override def write(f: PrimeDataFile): Unit = {
    if (shouldCook) f.writeString(valueOrDefaultValue)
  }

  override def read(f: PrimeDataFile): Unit = {
    if (shouldCook) value = Some(f.readString())
  }

  override def applyPatch(v: JsonNode): Unit = {
    value = Some(v.asText())
  }

  override def valueAsJson: JsonNode = TextNode.valueOf(valueOrDefaultValue)
}

class BooleanScriptProperty extends ScriptProperty[Boolean] {
  override def parseValue(node: NodeSeq): Boolean = node.text.toBoolean

  override def defaultDefault: Option[Boolean] = Some(false)

  override def write(f: PrimeDataFile): Unit = {
    if (shouldCook) f.writeBool(valueOrDefaultValue)
  }

  override def read(f: PrimeDataFile): Unit = {
    if (shouldCook) value = Some(f.readBool())
  }

  override def applyPatch(v: JsonNode): Unit = {
    value = Some(v.asBoolean())
  }

  override def valueAsJson: JsonNode = if (valueOrDefaultValue) BooleanNode.TRUE else BooleanNode.FALSE
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

class StructScriptProperty extends TemplatedScriptProperty[StructScriptProperty] {
  var typ: ScriptPropertyStructType = ScriptPropertyStructType.SINGLE

  var properties = mutable.LinkedHashMap[String, ScriptProperty[_]]()
  default = None

  override def parseValue(node: NodeSeq): StructScriptProperty = ???

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
    })
  }

  override def write(f: PrimeDataFile): Unit = {
    if (shouldCook) {
      if (typ == ScriptPropertyStructType.MULTI) {
        f.write32(properties.size)
      }
      for (prop <- properties) {
        prop._2.write(f)
      }
    }
  }

  override def read(f: PrimeDataFile): Unit = {
    if (shouldCook) {
      if (typ == ScriptPropertyStructType.MULTI) {
        val propCount = f.read32()
        if (propCount != properties.size) throw new IllegalArgumentException(s"Invalid property count (expected ${properties.size}, found $propCount)")
      }
      for (prop <- properties) {
        prop._2.read(f)
      }
    }
  }

  override def applyPatch(v: JsonNode): Unit = {
    for (field: String <- v.fieldNames().toIterator) {
      val prop = properties.getOrElse(field, throw new IllegalArgumentException(s"Uknown property $field"))
      prop.applyPatch(v.get(field))
    }
  }

  override def valueAsJson: JsonNode = {
    val node = PrimeJacksonMapper.mapper.createObjectNode()
    for ((id, prop) <- properties) {
      node.set(id, prop.valueAsJson)
    }
    node
  }
}

class EnumScriptProperty extends TemplatedScriptProperty[Int] {
  var enumerators = Seq[Enumerator]()

  override def defaultDefault: Option[Int] = Some(0)

  override def parseValue(node: NodeSeq): Int = DataTypeConversion.stringToLong(node.text).toInt

  override def write(f: PrimeDataFile): Unit = {
    if (shouldCook) f.write32(valueOrDefaultValue)
  }

  override def read(f: PrimeDataFile): Unit = {
    if (shouldCook) value = Some(f.read32())
  }

  override def applyPatch(v: JsonNode): Unit = {
    value = Some(DataTypeConversion.stringToLong(v.asText()).toInt)
  }

  override def valueAsJson: JsonNode = TextNode.valueOf("0x" + (valueOrDefaultValue & 0xFFFFFFFF).toHexString)
}

class BitfieldScriptProperty extends TemplatedScriptProperty[Int] {
  var flags = Seq[Flag]()

  override def parseValue(node: NodeSeq): Int = DataTypeConversion.stringToLong(node.text).toInt

  override def defaultDefault: Option[Int] = Some(0)

  override def write(f: PrimeDataFile): Unit = {
    if (shouldCook) f.write32(valueOrDefaultValue)
  }

  override def read(f: PrimeDataFile): Unit = {
    if (shouldCook) value = Some(f.read32())
  }

  override def applyPatch(v: JsonNode): Unit = {
    value = Some(DataTypeConversion.stringToLong(v.asText()).toInt)
  }

  override def valueAsJson: JsonNode = TextNode.valueOf("0x" + (valueOrDefaultValue & 0xFFFFFFFF).toHexString)
}

class ArrayScriptProperty extends TemplatedScriptProperty[Array[StructScriptProperty]] {
  var properties = Map[String, ScriptProperty[_]]()

  //Not sure how to do defaults here....
  //Also, no templated arrays are ever used, but they *should* work

  override def parseValue(node: NodeSeq): Array[StructScriptProperty] = ???

  override def fromXml(v: NodeSeq): Unit = {
    super.fromXml(v)

    (v \ "properties" \ "_").foreach(prop => {
      val id = (prop \ "@ID").text.trim
      if (properties.contains(id)) {
        properties(id).fromXml(prop)
      } else {
        val parsed = prop.as(ScriptProperty.determineType _)
        properties += parsed.ID -> parsed
      }
    })
  }

  override def write(f: PrimeDataFile): Unit = {
    if (shouldCook) {
      val actualValue: Array[StructScriptProperty] = valueOrDefaultValue
      f.write32(actualValue.length)
      for (v <- actualValue; prop <- v.properties) {
        prop._2.write(f)
      }
    }
  }

  override def read(f: PrimeDataFile): Unit = {
    if (shouldCook) {
      val count = f.read32()
      value = Some(new Array[StructScriptProperty](count))
      val actualValue: Array[StructScriptProperty] = valueOrDefaultValue
      for (i <- actualValue.indices) {
        val struct = new StructScriptProperty
        struct.properties = mutable.LinkedHashMap(properties.map(prop => prop._1 -> prop._2.copy()).toSeq: _*)
        struct.read(f)
        actualValue(i) = struct
      }
    }
  }

  override def applyPatch(v: JsonNode): Unit = ???
  override def valueAsJson: JsonNode = ???
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