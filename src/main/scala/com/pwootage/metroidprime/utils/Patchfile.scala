package com.pwootage.metroidprime.utils

import java.nio.file.{Files, Path}

import com.fasterxml.jackson.annotation.{JsonIgnore, JsonInclude, JsonSubTypes, JsonTypeInfo}
import com.fasterxml.jackson.databind.JsonNode
import com.pwootage.metroidprime.formats.common.PrimeVersion
import com.pwootage.metroidprime.formats.mrea.MREA
import com.pwootage.metroidprime.formats.scly.{Prime1ScriptObjectType, ScriptObjectConnection, ScriptObjectInstance}
import com.pwootage.metroidprime.templates.ScriptTemplate

import scala.collection.JavaConversions._

case class Patchfile(description: String,
                     patches: Seq[PatchAction],
                     author: Option[String] = None) {
  @JsonIgnore
  var patchfileLocation: Option[Path] = None
}

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(Array(
  new JsonSubTypes.Type(value = classOf[ReplaceFile], name = "ReplaceFile"),
  new JsonSubTypes.Type(value = classOf[BinaryPatch], name = "BinaryPatch"),
  new JsonSubTypes.Type(value = classOf[ScriptObjectPatch], name = "ScriptObject")
))
abstract class PatchAction(val filename: String,
                           val description: Option[String] = None) {

  def execute(primeVersion: PrimeVersion, patchfileDir: Path, src: Array[Byte]): Array[Byte]
  def isApplicable = true

}

case class ReplaceFile(_filename: String,
                       replacementFile: String) extends PatchAction(_filename) {

  override def execute(primeVersion: PrimeVersion, patchfileDir: Path, src: Array[Byte]): Array[Byte] = {
    Files.readAllBytes(patchfileDir.resolve(replacementFile))
  }

}

case class BinaryPatch(_filename: String,
                       start: Int,
                       newData: Array[Byte]) extends PatchAction(_filename) {

  override def execute(primeVersion: PrimeVersion, patchfileDir: Path, src: Array[Byte]): Array[Byte] = {
    val dest = src.clone()
    System.arraycopy(newData, 0, dest, start, newData.length)
    dest
  }

}

case class ScriptObjectPatch(_filename: String,
                             typ: String,
                             id: Int) extends PatchAction(_filename) {

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  var remove: Option[Boolean] = None

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  var add: Option[Boolean] = None

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  var layer: Option[Int] = None

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  var linksToRemove: List[ScriptObjectConnection] = List()

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  var linksToAdd: List[ScriptObjectConnection] = List()

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  var objectPatch: Option[JsonNode] = None

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  var binaryPatch: Option[Array[Byte]] = None

  def typeEnum(primeVersion: PrimeVersion) = primeVersion match {
    case PrimeVersion.PRIME_1 => Prime1ScriptObjectType.valueOf(typ)
  }

  override def execute(primeVersion: PrimeVersion, patchfileDir: Path, src: Array[Byte]): Array[Byte] = {
    val mrea = new MREA
    mrea.read(src)

    val scly = mrea.parseSCLY
    val objectType = typeEnum(primeVersion)

    if (add.getOrElse(false)) {
      val res = new ScriptObjectInstance
      res.id = id
      res.typ = objectType.id

      val template = res.typeEnum.template()

      applyPatchToTemplate(objectPatch.get, template)

      res.binaryData = template.toByteArray

      res.propertyCount = template.properties.length

      scly.layers(layer.getOrElse(0)).objects +:= res

    } else {
      for (layer <- scly.layers) {
        if (remove.getOrElse(false)) {
          layer.objects = layer.objects.filter(o => o.id == id && o.typeEnum == objectType)
        } else {
          if (!add.getOrElse(false)) {
            val objToModify = layer.objects
              .find(o => o.id == id && o.typeEnum == objectType)
              .getOrElse(throw new IllegalArgumentException("Unable to find script object to patch"))

            if (objectPatch.isDefined) {
              val template = objectType.template()

              if (template == null) {
                throw new IllegalArgumentException("Unknown actor parameters")
              }

              template.read(objToModify.binaryData)

              applyPatchToTemplate(objectPatch.get, template)
              objToModify.propertyCount = template.properties.length

              val newBinary = template.toByteArray

              objToModify.binaryData = newBinary
              //
              //            PrimeJacksonMapper.mapper.readerForUpdating(parsedObject).readValue(objectPatch.get)
              //            objToModify.binaryData = parsedObject.toByteArray
            }

            if (binaryPatch.isDefined) {
              objToModify.binaryData = binaryPatch.get
            }
          }

          //Done modifying
        }
        //Done with layer
      }
    }

    mrea.setSCLY(scly)
    mrea.toByteArray
  }


  def applyPatchToTemplate(patch: JsonNode, template: ScriptTemplate) = {
    for (field: String <- patch.fieldNames().toIterator) {
      val v = patch.get(field)
      template.properties.find(_.ID == field) match {
        case Some(x) => x.applyPatch(v)
        case None => throw new IllegalArgumentException(s"Unknown property $field")
      }
    }
  }
}
