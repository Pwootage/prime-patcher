package com.pwootage.metroidprime.randomizer

import java.io.{FileNotFoundException, IOException, RandomAccessFile}
import java.nio.file.Paths
import java.time.LocalDateTime
import java.util.Random

import better.files._
import com.pwootage.metroidprime.formats.common.PrimeVersion
import com.pwootage.metroidprime.formats.io.PrimeDataFile
import com.pwootage.metroidprime.formats.iso.GCIsoHeaders
import com.pwootage.metroidprime.formats.scly.Prime1ScriptObjectType
import com.pwootage.metroidprime.templates.ScriptTemplates._
import com.pwootage.metroidprime.utils._

class Randomizer(config: RandomizerConfig) {
  val prime1ItemLocations = {
    val src = resourceAsString("/randomizer/items/prime1ItemLocations.json")
    PrimeJacksonMapper.mapper.readValue(src, classOf[Array[Prime1ItemLocation]])
  }

  def naiveRandomize(isoFile: String): Unit = {
    val raf = new RandomAccessFile(Paths.get(isoFile).toFile, "rw")

    Logger.progress("Reading ISO header information...")

    val header = new GCIsoHeaders
    raf.seek(0)
    PrimeDataFile(Some(raf), Some(raf)).read(header)

    val gameID = DataTypeConversion.intContainingCharsAsStr(header.discHeader.gameCode) + DataTypeConversion.intContainingCharsAsStr(header.discHeader.makerCode)

    Logger.info(s"Found game ID $gameID version ${header.discHeader.version} (Internal name: ${header.discHeader.name})")

    val version = if (gameID == "GM8E01" || gameID == "GM8P01") {
      Some(PrimeVersion.PRIME_1)
    } else if (gameID == "G2ME01") {
      Some(PrimeVersion.PRIME_2)
    } else {
      None
    }
    raf.seek(0)

    version match {
      case Some(PrimeVersion.PRIME_1) => prime1Patches(raf)
      case _ => ???
    }
  }

  def prime1Patches(raf: RandomAccessFile) = {
    val seed = config.seed.getOrElse(new Random().nextInt())
    val rng = new Random(seed)

    var itemPool = {
      val src = resourceAsString("/randomizer/items/prime1Items.json")
      PrimeJacksonMapper.mapper.readValue(src, classOf[Array[Prime1Item]])
    }

    itemPool = itemPool.flatMap(item => {
      Array.fill(item.count)(new Prime1Item(item.name, 1, item.item, item.capacity, item.amount, item.model, item.animSet, item.animCharacter, item.rotation, item.xrayModel, item.xraySkin))
    })

    def removeItemFromPool(id: Int): Unit = {
      itemPool.filter(_.item == id).find(_.count > 0) match {
        case Some(x) => x.count -= 1
        case None => throw new IOException("Tried to remove more of an item than there is")
      }
    }

    for ((_, item) <- config.fixed) {
      removeItemFromPool(item.id)
    }

    var log =
      s"""
         |prime-patcher randomizer log for seed for $seed generated at ${LocalDateTime.now()} with config:
         |${PrimeJacksonMapper.pretty.writeValueAsString(config)}
      """.stripMargin.trim

    val patches = (for ((p1obj, index) <- prime1ItemLocations.zipWithIndex) yield {
      if (config.fixed.contains(index.toString)) {
        //Maybe handle this better later
        None
      } else {
        val possibleItems = itemPool.filter(_.count > 0)
        val item = possibleItems(rng.nextInt(possibleItems.length))
        item.count -= 1

        log += s"${p1obj.area},${p1obj.description},${item.name}\n"
        val objectPatch = PrimeJacksonMapper.mapper.createObjectNode()
          .put("0x06", item.item) //item
          .put("0x07", item.capacityInt.getOrElse(1)) //capacity
          .put("0x08", item.amountInt.getOrElse(1)) //amount

        if (config.invisibleItems) {
          objectPatch.put("0x0C", "0x" + DataTypeConversion.intToPaddedHexString(item.model)) //model
          //Particle doesn't change, so no need to patch it

          //TODO: get actual scales of items (I forgot to grab them)
          //          val scale = PrimeJacksonMapper.mapper.createObjectNode()
          //            .put("x", 0)
          //            .put("y", 0)
          //            .put("z", 0)
          //          objectPatch.set("0x03", scale)

          val animParams = PrimeJacksonMapper.mapper.createObjectNode()
            .put("animANCS", "0x" + DataTypeConversion.intToPaddedHexString(item.animSet))
            .put("character", item.animCharacterInt.getOrElse(0))
            .put("defaultAnim", 0)

          objectPatch.set("0x0D", animParams)

          val actorParams = PrimeJacksonMapper.mapper.createObjectNode()
            .put("0x02", DataTypeConversion.intToPaddedHexString(item.xrayModelInt.getOrElse(0xFFFFFFFF)) + ".CMDL") //xray model
            .put("0x03", DataTypeConversion.intToPaddedHexString(item.xraySkinInt.getOrElse(0xFFFFFFFF)) + ".CSKR") //xray skin
          //Thermal never changes

          //TODO: Scans
          val scannableParams = PrimeJacksonMapper.mapper.createObjectNode()
            .put("0x00", "ffffffff.SCAN")

          actorParams.set("0x01", scannableParams)

          objectPatch.set("0x0E", actorParams) //Actor params
        }

        val res = ScriptObjectPatch(p1obj.room, Prime1ScriptObjectType.Pickup.name(), p1obj.id)
        res.objectPatch = Some(objectPatch)
        res.description = Some(s"${p1obj.area} - ${p1obj.description}")

        Some(res)
      }
    }).flatten

    val patch = Patchfile(s"Randomizer seed $seed", patches, Some("prime-patcher"))

    config.patchFile.toFile.parent.createDirectories()
    config.patchFile.toFile.write(PrimeJacksonMapper.pretty.writeValueAsString(patch))
    config.logFile
      .map(_.replace("%SEED%", seed.toString))
      .foreach(file => {
        file.toFile.parent.createDirectories()
        file.toFile.write(log)
      })

    Logger.success(s"Successfully generated patch file for seed $seed")
  }

  def resourceAsString(path: String): String = {
    val in = getClass.getResourceAsStream(path)
    if (in == null) {
      throw new FileNotFoundException(path)
    }
    new String(in.bytes.toArray)
  }
}
