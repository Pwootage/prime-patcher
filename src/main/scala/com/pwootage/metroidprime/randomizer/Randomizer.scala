package com.pwootage.metroidprime.randomizer

import java.io.{ByteArrayOutputStream, FileNotFoundException, IOException, RandomAccessFile}
import java.nio.file.{Files, Paths, StandardOpenOption}
import java.time.LocalDateTime
import java.util.Random
import java.util.zip.InflaterOutputStream

import better.files._
import com.google.diff.diff_match_patch
import com.pwootage.metroidprime.formats.common.PrimeVersion
import com.pwootage.metroidprime.formats.io.PrimeDataFile
import com.pwootage.metroidprime.formats.iso.{FST, FileEntry, GCIsoHeaders}
import com.pwootage.metroidprime.formats.pak.{BasicResourceList, PAKFile, Resource}
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
    config.outDir.toFile.createDirectories()

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

        val res = ScriptObjectPatch(p1obj.room, Prime1ScriptObjectType.Pickup.name(), p1obj.id)
        res.objectPatch = Some(objectPatch)
        res.description = Some(s"${p1obj.area} - ${p1obj.description}")

        Some(res)
      }
    }).flatten ++ getPakPatches(PrimeVersion.PRIME_1, raf)

    val patch = Patchfile(s"Randomizer seed $seed", patches, Some("prime-patcher"))

    (config.outDir.toFile / "randomize.json").write(PrimeJacksonMapper.pretty.writeValueAsString(patch))
    (config.outDir.toFile / s"log$seed.log").write(log)

    Logger.success(s"Successfully generated patch file for seed $seed")
  }

  def resourceAsString(path: String): String = {
    val in = getClass.getResourceAsStream(path)
    if (in == null) {
      throw new FileNotFoundException(path)
    }
    new String(in.bytes.toArray)
  }

  def resourceAsBytes(path: String): Array[Byte] = {
    val in = getClass.getResourceAsStream(path)
    if (in == null) {
      throw new FileNotFoundException(path)
    }
    in.bytes.toArray
  }

  def getPakPatches(primeVersion: PrimeVersion, raf: RandomAccessFile): Seq[PatchAction] = {
    val header = new GCIsoHeaders
    raf.seek(0)
    PrimeDataFile(Some(raf), Some(raf)).read(header)

    val fst = new FST
    raf.seek(header.discHeader.fstOffset)
    PrimeDataFile(Some(raf), Some(raf)).read(fst)

    val paks = Seq(
      "metroid2.pak",
      "metroid3.pak",
      "metroid4.pak",
      "metroid5.pak",
      "metroid6.pak"
    ).flatMap(pak => fst.rootDirectoryEntry.fileChildren.find(_.name.toLowerCase == pak))

    val fileDepList = PrimeJacksonMapper.mapper.readValue(resourceAsString("/randomizer/deps-basic.json"), classOf[List[String]])
    val allDeps = fileDepList.map(v => DataTypeConversion.stringToLong("0x" + v).toInt).toSet

    //Copy files to dest dir
    val resourceDir = config.outDir.toFile / "res"
    resourceDir.createDirectories()

    Seq(
      "50535430.CMDL",
      "50535431.TXTR",
      "50535432.TXTR",
      "50535433.ANCS"
    ).foreach(f => {
      Files.write((resourceDir / f).toJava.toPath, resourceAsBytes("/randomizer/phazonsuit/" + f))
    })

    //Copy all required files into dest dir
    paks.foreach(pak => copyFileFromPak(primeVersion, raf, pak, allDeps))

    val filesInDepDir = Map[Int, Int](
      resourceDir.list.map(f => DataTypeConversion.strResourceToIdAndType(f.name)).toSeq: _*
    )

    val depsWithType = fileDepList.map(v => {
      val intid = DataTypeConversion.stringToLong("0x" + v).toInt
      DataTypeConversion.intPrimeResourceNameToStr(intid, filesInDepDir(intid))
    }).toSet ++ Seq(
      "50535430.CMDL",
      "50535431.TXTR",
      "50535432.TXTR",
      "50535433.ANCS"
    )

    def getPatchForPak(raf: RandomAccessFile, pakEntry: FileEntry): Seq[PatchAction] = {
      //Read PAK info
      val offset = pakEntry.offset
      raf.seek(offset)
      val pak = new PAKFile(primeVersion)
      pak.read(new PrimeDataFile(Some(raf), None))

      val filesInPak = pak.resources.map(_.idStr).toSet

      val needed = depsWithType -- filesInPak

      val originalBrl = pak.toBasicResourceList
      val brl = BasicResourceList(originalBrl.primeVersion, originalBrl.namedResources, {
        originalBrl.resources ++ needed
      })

      val differ = new diff_match_patch

      val pakDiff = differ.diff_main(
        originalBrl.resources.mkString("\n"),
        brl.resources.mkString("\n")
      )

      differ.diff_cleanupEfficiency(pakDiff)
      val pakPatch = differ.patch_make(pakDiff)

      Seq(PakfileResourceListPatch(
        pakEntry.name,
        differ.patch_toText(pakPatch)
      ))
    }

    paks.flatMap(pak => getPatchForPak(raf, pak))
  }

  def copyFileFromPak(primeVersion: PrimeVersion, raf: RandomAccessFile, pakFileEntry: FileEntry, files: Set[Int]) = {
    val resourceDir = config.outDir.toFile / "res"

    //Read PAK info
    val offset = pakFileEntry.offset
    raf.seek(offset)
    val pak = new PAKFile(primeVersion)
    pak.read(new PrimeDataFile(Some(raf), None))

    val resources = pak.resources
      .filter(r => files.contains(r.id))
      .filter(r => (resourceDir / r.idStr).notExists)

    def copyResource(resource: Resource): Unit = {
      Logger.progressResetLine("Copying " + resource.idStr + " from " + pakFileEntry.name)
      raf.seek(offset + resource.offset)
      val out = Files.newOutputStream((resourceDir / resource.idStr).toJava.toPath, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)
      if (resource.compressed) {
        val decompressedSize = raf.readInt()

        if (primeVersion == PrimeVersion.PRIME_1) {
          val resourceStart = raf.getFilePointer
          val decompressedOut = new InflaterOutputStream(out)
          IOUtils.copyBytes(raf, resource.size - 4, decompressedOut)
          decompressedOut.flush()
          val resourceEnd = raf.getFilePointer
          val bytesRead = resourceEnd - resourceStart
          if (bytesRead != resource.size - 4) {
            throw new IOException("Read incorrect number of bytes from original file")
          }
        } else if (primeVersion == PrimeVersion.PRIME_2) {
          IOUtils.decompressSegmentedLZOStream(new RandomAccessFileInputStream(raf), out, decompressedSize)
        } else {
          throw new Error("I did something wrong D:")
        }
      } else {
        IOUtils.copyBytes(raf, resource.size, out)
      }
      out.close()
    }

    resources.foreach(copyResource)
  }
}
