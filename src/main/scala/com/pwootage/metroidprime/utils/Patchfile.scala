package com.pwootage.metroidprime.utils

import java.nio.file.{Files, Path}

import com.fasterxml.jackson.annotation.{JsonIgnore, JsonSubTypes, JsonTypeInfo}

case class Patchfile(description: String,
                     patches: Seq[PatchAction],
                     author: Option[String] = None) {
  @JsonIgnore
  var patchfileLocation: Option[Path] = None
}

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(Array(
  new JsonSubTypes.Type(value = classOf[ReplaceFile], name = "ReplaceFile")
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
