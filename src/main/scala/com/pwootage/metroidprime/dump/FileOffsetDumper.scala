package com.pwootage.metroidprime.dump

import java.io.{DataInputStream, RandomAccessFile}
import java.nio.file.{Files, Path, Paths}

import com.pwootage.metroidprime.formats.common.{Face, PrimeFileType, PrimeVersion}
import com.pwootage.metroidprime.formats.io.PrimeDataFile
import com.pwootage.metroidprime.formats.iso.{FST, FileDirectory, GCIsoHeaders}
import com.pwootage.metroidprime.formats.mlvl.{Area, MLVL}
import com.pwootage.metroidprime.formats.mrea.MREA
import com.pwootage.metroidprime.formats.mrea.collision.Collision
import com.pwootage.metroidprime.formats.pak.PAKFile
import com.pwootage.metroidprime.utils.{DataTypeConversion, FileLocator, Logger, PrimeJacksonMapper}

class FileOffsetDumper() {
  def dump(srcPath: String, destPath: String): Unit = {
    val dest = Paths.get(destPath)
    Files.createDirectories(dest)

    val raf = new RandomAccessFile(Paths.get(srcPath).toFile, "rw")
    Logger.progress("Reading ISO header information...")

    val header = new GCIsoHeaders
    raf.seek(0)
    PrimeDataFile(Some(raf), Some(raf)).read(header)

    val fst = new FST
    raf.seek(header.discHeader.fstOffset)
    PrimeDataFile(Some(raf), Some(raf)).read(fst)

    Logger.info("Writing FST offsets")
    Files.write(dest.resolve("fst.json"), PrimeJacksonMapper.pretty.writeValueAsBytes(fst.rootDirectoryEntry))

    extractPakOffsets(raf, fst.rootDirectoryEntry, dest)
  }

  def extractPakOffsets(raf: RandomAccessFile, dir: FileDirectory, dest: Path): Unit = {
    for (file <- dir.fileChildren.filter(_.name.toLowerCase.endsWith(".pak"))) {
      Logger.progress(s"Dumping offsets from ${file.name}")

      val pak = new PAKFile(PrimeVersion.PRIME_1) //Doesn't matter, we're just looking for a compressed file
      raf.seek(file.offset)
      pak.read(new PrimeDataFile(Some(raf), None))

      val outFile = dest.resolve(file.name + ".json")
      Files.write(outFile, PrimeJacksonMapper.pretty.writeValueAsBytes(pak.resources))
    }

    //There arn't actually any nested PAKs but may as well support it
    for (folder <- dir.directoryChildren) {
      extractPakOffsets(raf, folder, dest.resolve(folder.name))
    }
  }
}
