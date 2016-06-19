package com.pwootage.metroidprime.utils

import java.nio.file.{Files, Path}
import java.util

import com.fasterxml.jackson.annotation.{JsonIgnore, JsonInclude, JsonSubTypes, JsonTypeInfo}
import com.fasterxml.jackson.databind.JsonNode
import com.pwootage.metroidprime.formats.common.PrimeVersion
import com.pwootage.metroidprime.formats.mrea.MREA
import com.pwootage.metroidprime.formats.scly.prime1ScriptObjects.{Pickup, ScriptObjectInstanceBase}
import com.pwootage.metroidprime.formats.scly.{Prime1ScriptObjectType, SCLY, ScriptObjectConnection, ScriptObjectInstance}

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

    for (layer <- scly.layers) {
      if (remove.getOrElse(false)) {
        layer.objects = layer.objects.filter(o => o.id == id && o.typeEnum == objectType)
      } else {
        if (add.getOrElse(false)) {
          val res = new ScriptObjectInstance
          res.id = id
          res.typ = objectType.id
          //TODO: prime 2 property count nonsense
        } else {
          val objToModify = layer.objects
            .find(o => o.id == id && o.typeEnum == objectType)
            .getOrElse(throw new IllegalArgumentException("Unable to find script object to patch"))

          if (objectPatch.isDefined) {
            val parsedObject: ScriptObjectInstanceBase = objectType.toObject(objToModify)

            if (parsedObject == null) {
              throw new IllegalArgumentException("This object type is not yet supported")
            }

            PrimeJacksonMapper.mapper.readerForUpdating(parsedObject).readValue(objectPatch.get)
            objToModify.binaryData = parsedObject.toByteArray
          }

          if (binaryPatch.isDefined) {
            objToModify.binaryData = binaryPatch.get
          }


        }
      }
    }

    mrea.setSCLY(scly)
    mrea.toByteArray
  }

}
