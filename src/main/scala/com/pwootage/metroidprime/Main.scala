package com.pwootage.metroidprime

import java.io.{FileWriter, RandomAccessFile}
import java.nio.file.{Files, Paths}

import com.pwootage.metroidprime.dump.{Extractor, PickupDumper}
import com.pwootage.metroidprime.formats.io.PrimeDataFile
import com.pwootage.metroidprime.formats.mrea.MREA
import com.pwootage.metroidprime.formats.scly.Prime1ScriptObjectType
import com.pwootage.metroidprime.formats.scly.prime1ScriptObjects.Pickup
import com.pwootage.metroidprime.randomizer.Randomizer
import com.pwootage.metroidprime.utils.FileIdentifier
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
      val force = toggle(name="force", short='f', default=Some(false))
      val destDir = trailArg[String](descr = "Where to put the results")
      val srcFiles = trailArg[List[String]](descr = "ISOs or PAKs to parse")
    }
    addSubcommand(extract)

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
      case conf.test => test()
    }
  }

  def dump(conf: PatcherConf): Unit = {
    conf.dump.what() match {
      case "pickups" => PickupDumper.dump(conf.dump.searchDirectory(), conf.dump.outDir())
    }
  }
  def extract(conf: PatcherConf): Unit = {
    for (file <- conf.extract.srcFiles()) {
      if (FileIdentifier.isISO(file)) {
        Extractor.extractIso(file, conf.extract.destDir(), conf.extract.force())
      }
    }
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

    Files.deleteIfExists(Paths.get("out/coll.obj"))
    val objOut = new FileWriter("out/coll.obj", false)

    objOut.write(s"# Vertexes (${coll.verts.length}\n")
    for (v <- coll.verts) {
      objOut.write(s"v ${v.x} ${v.y} ${v.z}\n")
    }

    var uhhh = 0
    objOut.write(s"# Faces (${coll.faces.length}\n")
    for ((f, i) <- coll.faces.zipWithIndex) {
      val flags = coll.collisionMaterialFlags(coll.facePropertyIndices(i))
      val line1 = coll.lines(f.ind1)
      val line2 = coll.lines(f.ind2)
      val line3 = coll.lines(f.ind3)
      if (Seq(line1.ind1, line1.ind2, line2.ind1, line2.ind2, line3.ind1, line3.ind2).exists(_ > coll.verts.length)) {
        uhhh += 1
        objOut.write("#")
      }
      val (l1a, l1b) = (coll.verts(line1.ind1), coll.verts(line1.ind2))
      val (l2a, l2b) = (coll.verts(line2.ind1), coll.verts(line2.ind2))
      val (l3a, l3b) = (coll.verts(line3.ind1), coll.verts(line3.ind2))

      val i1 = line1.ind1
      val (i2, otherLine) = {
        if (line1.ind1 == line2.ind1) {
          (line2.ind2, line3)
        } else if (line1.ind1 == line2.ind2) {
          (line2.ind1, line3)
        } else if (line1.ind1 == line3.ind1) {
          (line3.ind2, line2)
        } else {
          (line3.ind1, line2)
        }
      }
      val i3 = {
        if (i2 == otherLine.ind1) {
          otherLine.ind2
        } else {
          otherLine.ind1
        }
      }
      if ((flags & 0x2000000) > 0) {
        objOut.write(s"f ${i1 + 1} ${i2 + 1} ${i3 + 1}\n")
      } else {
        objOut.write(s"f ${i3 + 1} ${i2 + 1} ${i1 + 1}\n")
      }
    }
    println(s"Uhh: $uhhh")

    objOut.close()

    print("done2")
  }
}
