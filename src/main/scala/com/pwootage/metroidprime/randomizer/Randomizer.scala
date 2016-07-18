package com.pwootage.metroidprime.randomizer

import java.io.FileNotFoundException

import better.files._
import com.pwootage.metroidprime.formats.scly.Prime1ScriptObjectType
import com.pwootage.metroidprime.utils._

class Randomizer {
  val prime1ItemObjects = {
    val src = resourceAsString("randomizer/items/prime1items.json")
    PrimeJacksonMapper.mapper.readValue(src, classOf[Array[ItemScriptObject]])
  }

  def naiveRandomize(primeVersion: String, outFile: String): Unit = {
    primeVersion match {
      case "mp1" => prime1Patches(outFile)
      case _ => ???
    }
  }

  def prime1Patches(outFile: String) = {
    val patches = for (p1obj <- prime1ItemObjects) yield {

      val scale = PrimeJacksonMapper.mapper.createObjectNode()
        .put("x", 5)
        .put("y", 5)
        .put("z", 5)
      val objectPatch = PrimeJacksonMapper.mapper.createObjectNode()
        .put("0x06", "0x04") //item (missile)
        .put("0x07", "0x01") //capacity
        .put("0x08", "0x01") //amount
      objectPatch.set("0x03", scale) //scale

      val res = ScriptObjectPatch(p1obj.room, Prime1ScriptObjectType.Pickup.name(), p1obj.id)
      res.objectPatch = Some(objectPatch)
      res.description = Some(s"${p1obj.area} - ${p1obj.description}")

      res
    }

    val patch = Patchfile("Item scale HUGE", patches, Some("Pwootage"))

    outFile.toFile.write(PrimeJacksonMapper.mapper.writeValueAsString(patch))
  }

  def resourceAsString(path: String): String = {
    val in = getClass.getClassLoader.getResourceAsStream(path)
    if (in == null) {
      throw new FileNotFoundException(path)
    }
    new String(in.bytes.toArray)
  }
}
