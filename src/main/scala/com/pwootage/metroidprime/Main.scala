package com.pwootage.metroidprime

import java.io.{FileWriter, RandomAccessFile}
import java.nio.file.{Files, Paths}

import com.pwootage.metroidprime.dump._
import com.pwootage.metroidprime.formats.io.PrimeDataFile
import com.pwootage.metroidprime.formats.mrea.MREA
import com.pwootage.metroidprime.formats.scly.Prime1ScriptObjectType
import com.pwootage.metroidprime.formats.scly.prime1ScriptObjects.Pickup
import com.pwootage.metroidprime.randomizer.Randomizer
import com.pwootage.metroidprime.utils.{FileIdentifier, Patchfile, PrimeJacksonMapper}
import org.rogach.scallop.{ScallopConf, Subcommand}

object Main {

  class PatcherConf(args: Seq[String]) extends ScallopConf(args) {
    val dump = new Subcommand("dump") {
      val what = trailArg[String](descr = "What to dump")
      val searchDirectory = trailArg[String](descr = "Path to search for MREAs (should contain the various extracted PAKs")
      val outDir = trailArg[String](descr = "Output directory")
    }
    addSubcommand(dump)

    val extract = new Subcommand("extract") {
      val force = toggle(name="force", short='f', default=Some(false), descrYes = "Overwrite existing files")
      val extractPaks = toggle(name="paks", short='p', default = Some(false), descrYes = "Extract PAKs from ISOs")
      val quieter = toggle(name="quieter", short='q', default = Some(false), descrYes = "Squelch constant PAK extraction messages (will update every 500 files)")
      val destDir = trailArg[String](descr = "Where to put the results")
      val srcFiles = trailArg[List[String]](descr = "ISOs or PAKs to parse")
    }
    addSubcommand(extract)

    val repack =  new Subcommand("repack") {
      val force = toggle(name="force", short='f', default=Some(false), descrYes = "Overwrite existing file")
      val quieter = toggle(name="quieter", short='q', default = Some(false), descrYes = "Squelch constant PAK repacking messages (will update every 500 files)")
      val srcDir = trailArg[String](descr = "Source directory - can be PAK root (list.json) or ISO root (info.json)")
      val outFile = trailArg[String](descr = "Target File (.pak or .iso)")
    }
    addSubcommand(repack)

    val patch =  new Subcommand("patch") {
      val force = toggle(name="force", short='f', default=Some(false), descrYes = "Overwrite existing file")
      val quieter = toggle(name="quieter", short='q', default = Some(false), descrYes = "Squelch constant PAK extraction/repacking messages")
      val srcFile = trailArg[String](descr = "Source ISO")
      val outFile = trailArg[String](descr = "Target ISO")
      val patchfiles = trailArg[List[String]](descr = "Patchfiles to use")
    }
    addSubcommand(patch)

    val randomize = new Subcommand("randomize") {
      val dirWithPAKs = trailArg[String]()
    }
    addSubcommand(randomize)

    val test = new Subcommand("test") {

    }
    addSubcommand(test)

    verify()
  }


  def main(args: Array[String]) {
    val conf = new PatcherConf(args)

    if (conf.subcommand.isEmpty) {
      conf.printHelp()
      System.exit(1)
    }
    val command = conf.subcommand.get

    command match {
      case conf.dump => dump(conf)
      case conf.randomize => randomize(conf)
      case conf.extract => extract(conf)
      case conf.repack => repack(conf)
      case conf.patch => patch(conf)
      case conf.test => test()
    }
  }

  def dump(conf: PatcherConf): Unit = {
    conf.dump.what() match {
      case "pickups" => PickupDumper.dump(conf.dump.searchDirectory(), conf.dump.outDir())
      case "collision" => new CollisionDumper().dump(conf.dump.searchDirectory(), conf.dump.outDir())
    }
  }
  def extract(conf: PatcherConf): Unit = {
    for (file <- conf.extract.srcFiles()) {
      if (FileIdentifier.isISO(file)) {
        new Extractor(conf.extract.destDir(), conf.extract.force(), conf.extract.extractPaks(), conf.extract.quieter()).extractIso(file)
      } else {
        new Extractor(conf.extract.destDir(), conf.extract.force(), conf.extract.extractPaks(), conf.extract.quieter()).extractPak(file)
      }
    }
  }

  def repack(conf: PatcherConf): Unit = {
    new Repacker(conf.repack.outFile(), conf.repack.force(), conf.repack.quieter()).repack(conf.repack.srcDir())
  }

  def patch(conf: PatcherConf): Unit = {
    val patchfiles = conf.patch.patchfiles().map(f => {
      val path = Paths.get(f)
      val bytes = Files.readAllBytes(path)
      val res = PrimeJacksonMapper.mapper.readValue(bytes, classOf[Patchfile])
      res.patchfileLocation = Some(path)
      res
    })

    new Patcher(conf.patch.outFile(), conf.patch.force(), conf.patch.quieter(), patchfiles).patch(conf.patch.srcFile())
  }

  def randomize(conf: PatcherConf): Unit = {
    (new Randomizer).naiveRandomize(conf.randomize.dirWithPAKs())
  }

  def test(): Unit = {
    val file = Paths.get("K:/roms/gc/mp-extracted/mp1/Metroid4-pak/b2701146.MREA")
    val bytes = Files.readAllBytes(file)
    val mrea = new MREA
    mrea.read(bytes)
    val bytes2 = mrea.toByteArray

    if (bytes.length != bytes2.length) {
      println(s"invalid lengths: ${bytes.length} ${bytes2.length}")
    }

    for (i <- bytes.indices) {
      if (bytes(i) != bytes2(i)) {
        println(s"invalid at pos ${i.toHexString} ${bytes(i)} ${bytes2(i)}")
      }
    }
  }

  def oldMain(): Unit = {
    //    val file = "K:/roms/gc/mp-extracted/mp1/Metroid2-pak/83f6ff6f.MLVL"
    //    val file = "K:/roms/gc/mp-extracted/mp2/Metroid1-pak/3bfa3eff.MLVL"
    val file = "K:/roms/gc/mp-extracted/mp1/Metroid4-pak/b2701146.MREA"
    val raf = new RandomAccessFile(file, "r")
    val pf = new PrimeDataFile(raf, raf)

    val mrea = new MREA
    mrea.read(pf)

    raf.close()

    val out = "out/out.MREA"
    val outPath = Paths.get(out)
    Files.createDirectories(outPath.getParent)
    val raf2 = new RandomAccessFile(out, "rw")
    raf2.getChannel.truncate(0) //Clear out file
    val pf2 = new PrimeDataFile(raf2, raf2)
    mrea.write(pf2)
    raf2.close()
    println("done")

    val scly = mrea.parseSCLY

    scly.layers(0).objects.find(_.typ == Prime1ScriptObjectType.Pickup.id).foreach(item => {
      Files.write(Paths.get("out/missile1.item"), item.binaryData)
      val pickup = new Pickup
      pickup.read(item.pdfForBody)
      println(pickup)
    })

    println("done3")

    Files.write(Paths.get("out/out.COLL"), mrea.rawSections(mrea.collisionSection))

    val coll = mrea.parseCollision
  }
}
