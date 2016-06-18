package com.pwootage.metroidprime.utils

import java.nio.file.{Files, Path}
import java.util

import com.fasterxml.jackson.annotation.{JsonIgnore, JsonSubTypes, JsonTypeInfo}

case class Patchfile(description: String,
                     patches: Seq[PatchAction],
                     author: Option[String] = None) {
  @JsonIgnore
  var patchfileLocation: Option[Path] = None
}

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(Array(
  new JsonSubTypes.Type(value = classOf[ReplaceFile], name = "ReplaceFile"),
  new JsonSubTypes.Type(value = classOf[BinaryPatch], name = "BinaryPatch")
))
abstract class PatchAction(val filename: String,
                           val description: Option[String] = None) {

  def execute(patchfileDir: Path, src: Array[Byte]): Array[Byte]
  def isApplicable = true

}

case class ReplaceFile(_filename: String,
                       replacementFile: String) extends PatchAction(_filename) {

  override def execute(patchfileDir: Path, src: Array[Byte]): Array[Byte] = {
    Files.readAllBytes(patchfileDir.resolve(replacementFile))
  }

}

case class BinaryPatch(_filename: String,
                       start: Int,
                       newData: Array[Byte]) extends PatchAction(_filename) {

  override def execute(patchfileDir: Path, src: Array[Byte]): Array[Byte] = {
    val dest = src.clone()
    System.arraycopy(newData, 0, dest, start, newData.length)
    dest
  }

}
