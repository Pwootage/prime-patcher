package com.pwootage.metroidprime.randomizer

import java.nio.file.Files

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import com.pwootage.metroidprime.formats.common.PrimeVersion
import com.pwootage.metroidprime.formats.mrea.MREA
import com.pwootage.metroidprime.formats.scly.{Prime1ScriptObjectType, SCLY, ScriptObjectInstance}
import com.pwootage.metroidprime.utils.FileLocator

import scala.util.Random

class Randomizer {
  /*val objectMapper = new ObjectMapper() with ScalaObjectMapper
  objectMapper.registerModule(DefaultScalaModule)

  val pickupsPrime1 = objectMapper.readValue[Seq[Pickup]](getClass.getResourceAsStream("/itemPatches/pickups.json"))
  val objectsPrime1 = objectMapper.readValue[Seq[ScriptObjectInstance]](getClass.getResourceAsStream("/itemPatches/objects.json"))

  val objectsPrime1ByID = Map(objectsPrime1.map(o => (o.id, o)): _*)

  var currentUsedItemID = 0

  def naiveRandomize(baseDir: String): Unit = {
    val mreaFiles = FileLocator.findFilesInBasePathWithExtension(baseDir, "MREA")

    for (file <- mreaFiles) {
      val in = Files.readAllBytes(file)

      println(s"Parsing file $file")
      val mrea = new MREA
      mrea.read(in)

      val scly = mrea.parseSCLY

      mrea.primeVersion match {
        case PrimeVersion.PRIME_1 => modifyPrime1(scly)
        case _ => ???
      }

      mrea.setSCLY(scly)

      println(s"Rewriting MREA $file")
      Files.write(file, mrea.toByteArray)
    }

    println(s"Changed $currentUsedItemID items")
  }

  def modifyPrime1(scly: SCLY) = {
    val endObjects = Random.shuffle(pickupsPrime1)
    for (layer <- scly.layers; i <- layer.objects.indices) {
      val destObj = layer.objects(i)
      if (destObj.typeEnum == Prime1ScriptObjectType.Pickup) {
        val pickup = new Pickup
        pickup.read(destObj.pdfForBody)
        if (pickup.capacity > 0) {
          val newItem = endObjects(currentUsedItemID)
          currentUsedItemID += 1
          newItem.pos = pickup.pos
          newItem.active = pickup.active
          layer.objects(i).binaryData = newItem.toByteArray
        }
      }
    }
  }*/
}
