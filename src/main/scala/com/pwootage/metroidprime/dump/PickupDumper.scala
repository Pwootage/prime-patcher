package com.pwootage.metroidprime.dump

import java.io.{BufferedWriter, DataInputStream, FileInputStream}
import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.{Files, Path, Paths}
import java.util.function.BiPredicate
import java.util.stream.Collectors

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.pwootage.metroidprime.formats.common.PrimeVersion
import com.pwootage.metroidprime.formats.io.PrimeDataFile
import com.pwootage.metroidprime.formats.mrea.MREA
import com.pwootage.metroidprime.formats.scly.{Prime1ScriptObjectType, SCLY}
import com.pwootage.metroidprime.formats.scly.prime1ScriptObjects.Pickup
import com.pwootage.metroidprime.utils.FileLocator
import org.rogach.scallop.ScallopOption

import scala.collection.JavaConversions._

object PickupDumper {
  def dump(searchDir: String, outDir: String): Unit = {
    val files = FileLocator.findFilesInBasePathWithExtension(searchDir, "MREA")

    val (objects, pickups) = (for (file <- files) yield {
      println(s"Parsing file $file")

      val in = Files.newInputStream(file)
      val din = new DataInputStream(in)
      val pdf = new PrimeDataFile(din)

      val mrea = new MREA
      mrea.read(pdf)

      val scly = mrea.parseSCLY

      mrea.primeVersion match {
        case PrimeVersion.PRIME_1 => dumpPrime1(outDir, scly)
        case _ => ???
      }
    }).flatten.filter(_._2.capacity > 0).unzip

    println(s"Found ${pickups.length} capacity-bearing pickups")

    Files.createDirectories(Paths.get(outDir))

    val mapper = new ObjectMapper()
    mapper.registerModule(DefaultScalaModule)

    Files.write(Paths.get(outDir, "pickups.json"), mapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(pickups))
    Files.write(Paths.get(outDir, "objects.json"), mapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(objects))
  }
  def dumpPrime1(outDir: String, scly: SCLY) =
    for (layer <- scly.layers; obj <- layer.objects
         if obj.typeEnum == Prime1ScriptObjectType.Pickup) yield {
      val pickup = new Pickup
      pickup.read(obj.binaryData)
      (obj, pickup)
    }
}