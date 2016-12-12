package com.pwootage.metroidprime.utils

import better.files._
import java.io._
import java.nio.charset.StandardCharsets
import java.nio.{ByteBuffer, IntBuffer}
import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.{Files, Path, Paths, StandardOpenOption}
import java.util.function.BiPredicate
import java.util.stream.Collectors
import java.util.zip.InflaterOutputStream

import com.pwootage.metroidprime.formats.common.PrimeVersion
import com.pwootage.metroidprime.formats.io.PrimeDataFile
import com.pwootage.metroidprime.formats.pak.PAKFile
import com.pwootage.metroidprime.randomizer.Prime1Item

import scala.collection.JavaConversions._
import scala.collection.immutable.Queue

object FileDepFinder {
  def main(args: Array[String]): Unit = {
    var itemPool = {
      val src = resourceAsString("/randomizer/items/prime1Items.json")
      PrimeJacksonMapper.mapper.readValue(src, classOf[Array[Prime1Item]])
    }

    var knownFilesWeNeedToFind = (
      itemPool.map(_.animSet)
        ++ itemPool.map(_.model)
        ++ itemPool.flatMap(_.xrayModelInt)
        ++ itemPool.flatMap(_.xraySkinInt)
      ).toSet

    var fileDeps = Map[Int, Set[Int]]()
    var paksLookedIn = Set[String]()

    def findDepsInPak(pak: String) = {
      var filesToGetDepsFor = Queue[Int](
        (knownFilesWeNeedToFind -- fileDeps.keySet).toSeq: _*
      )
      paksLookedIn += pak
      var itemDepsFound = fileDeps.keySet

      while (filesToGetDepsFor.nonEmpty) {
        var (next, newQueue) = filesToGetDepsFor.dequeue
        filesToGetDepsFor = newQueue

        if (!itemDepsFound.contains(next)) {
          val deps = findDepsOfFileInPak(DataTypeConversion.intToPaddedHexString(next), pak)
          itemDepsFound += next
          if (deps.isDefined) {
            fileDeps += next -> deps.get
            val newDeps = deps.get -- itemDepsFound
            filesToGetDepsFor = filesToGetDepsFor.enqueue(newDeps)
            knownFilesWeNeedToFind ++= newDeps
            Logger.progressResetLine(newDeps.size + " new deps, " + filesToGetDepsFor.length + " in queue, " + (knownFilesWeNeedToFind.size - fileDeps.size) + " left")
          }
        }
      }
    }
    Seq(
      "out/mp1/Metroid4.pak",
      "out/mp1/Metroid2.pak",
      "out/mp1/Metroid3.pak",
      "out/mp1/Metroid5.pak",
      "out/mp1/Metroid6.pak"
    ).foreach(findDepsInPak)

    val json = PrimeJacksonMapper.pretty.writeValueAsString(fileDeps.map(tuple => {
      val (key, value) = tuple
      DataTypeConversion.intToPaddedHexString(key) -> value.map(DataTypeConversion.intToPaddedHexString)
    }))
    Logger.success(json)
    Logger.success(paksLookedIn.toString())
    Files.write(Paths.get("out/deps.json"), json.getBytes(StandardCharsets.UTF_8))
  }

  def resourceAsString(path: String): String = {
    val in = getClass.getResourceAsStream(path)
    if (in == null) {
      throw new FileNotFoundException(path)
    }
    new String(in.bytes.toArray)
  }

  def findDepsOfFileInPak(fileIDStr: String, pakPath: String): Option[Set[Int]] = {
    val pakFile = Paths.get(pakPath)
    val raf = new RandomAccessFile(pakFile.toFile, "r")
    raf.seek(0)

    val primeVersion = findPakVersion(raf)

    raf.seek(0)
    val offset = raf.getFilePointer
    val pak = new PAKFile(primeVersion)
    pak.read(new PrimeDataFile(Some(raf), None))

    val (fileID, _) = DataTypeConversion.strResourceToIdAndType(fileIDStr + ".CMDL")

    val resourceBytes: Array[Byte] = pak.resources.find(r => r.id == fileID) match {
      case None =>
        Logger.error("Can't find resource: " + DataTypeConversion.intToPaddedHexString(fileID) + " in PAK " + pakPath)
        return None
        Array[Byte]()
      case Some(resource) =>
        raf.seek(offset + resource.offset)
        val out = new ByteArrayOutputStream()
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
        out.toByteArray
    }

    val resourceIdSet = pak.resources.map(_.id).toSet

    val buff = ByteBuffer.wrap(resourceBytes).asIntBuffer()
    var ids = Set[Int]()
    for (i <- 0 until resourceBytes.length / 4) {
      var maybeID = buff.get()
      if (resourceIdSet.contains(maybeID)) {
        ids += maybeID
      }
    }

    Logger.success("Done")
    val idStr = ids.map(DataTypeConversion.intToPaddedHexString).mkString(" ")
    Logger.success(fileID + ":" + idStr)

    raf.close()

    Some(ids)
  }

  private def findPakVersion(raf: RandomAccessFile): PrimeVersion = {
    val pak = new PAKFile(PrimeVersion.PRIME_1) //Doesn't matter, we're just looking for a compressed file
    pak.read(new PrimeDataFile(Some(raf), None))

    val compressedResource = pak.resources.find(_.compressed)

    compressedResource match {
      case None =>
        Logger.error("No compressed files! Unable to determine source version.")
        System.exit(1)
        PrimeVersion.PRIME_1 // Unreacable code
      case Some(r) =>
        raf.seek(r.offset + 4)
        val magic = raf.readUnsignedShort()
        if (magic == 0x78DA) {
          PrimeVersion.PRIME_1
        } else if (magic <= 0x4000) {
          PrimeVersion.PRIME_2
        } else {
          Logger.error("Unable to determine Prime Version - no ZLib header or valid block size found")
          System.exit(1)
          PrimeVersion.PRIME_1 // Unreacable code
        }
    }
  }
}
