package com.pwootage.metroidprime.dump

import java.io.{DataInputStream, RandomAccessFile}
import java.nio.ByteBuffer
import java.nio.file.{Files, Path, Paths, StandardOpenOption}

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.util.ArrayBuilders.ByteBuilder
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.pwootage.metroidprime.formats.io.PrimeDataFile
import com.pwootage.metroidprime.formats.iso.{FST, FileDirectory, FileEntry, GCIsoHeaders}
import com.pwootage.metroidprime.utils.{ByteDiffer, DataTypeConversion, Logger, PrimeJacksonMapper}
import com.sun.deploy.util.SyncFileAccess.RandomAccessFileLock

import scala.io.StdIn

object Extractor {
  def extractIso(fileName: String, targetDirectory: String, force: Boolean): Unit = {
    val file = Paths.get(fileName)
    val target = Paths.get(targetDirectory)
    if (Files.exists(target)) {
      if (Files.isDirectory(target)) {
        if (!force) {
          Logger.error("Target directory already exists; pass -f to force overwrite")
          System.exit(1)
        } else {
          Logger.info("Target directory already exits; force specified, overwriting existing files")
        }
      } else {
        Logger.error("Target file exists and is not a directory")
        System.exit(1)
      }
    } else {
      Files.createDirectories(target)
    }
    val raf = new RandomAccessFile(file.toFile, "rw")

    Logger.progress("Reading ISO header information...")

    val header = new GCIsoHeaders
    raf.seek(0)
    PrimeDataFile(Some(raf), Some(raf)).read(header)

    val gameID = DataTypeConversion.intContainingCharsAsStr(header.discHeader.gameCode) + DataTypeConversion.intContainingCharsAsStr(header.discHeader.makerCode)

    Logger.info(s"Found game ID $gameID version ${header.discHeader.version} (Internal name: ${header.discHeader.name})")

    Logger.progress("Parsing file structure....")
    val fst = new FST
    raf.seek(header.discHeader.fstOffset)
    PrimeDataFile(Some(raf), Some(raf)).read(fst)

    Logger.info(s"Found ${fst.rootDirectoryEntry.recursivelyCalculateEntryCount + 1} files and folders")

    //Write some header info to files so we can parse it again later
    Files.write(target.resolve("info.json"), PrimeJacksonMapper.pretty.writeValueAsBytes(header))
    recursivelyExtractFiles(raf, fst.rootDirectoryEntry, target)

    raf.close()

    Logger.success("Done")
  }

  def recursivelyExtractFiles(raf: RandomAccessFile, dir: FileDirectory, targetDir: Path): Unit = {
    Files.createDirectories(targetDir)
    for (file <- dir.fileChildren) {
      val sizeString = DataTypeConversion.bytesToReadable(file.length)
      Logger.progress(s"${file.name} ($sizeString)")
      raf.seek(file.offset)
      val buff = new Array[Byte](4096)
      val out = Files.newOutputStream(targetDir.resolve(file.name), StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)
      var totalRead = 0
      while (totalRead < file.length) {
        val toRead = Math.min(buff.length, file.length - totalRead)
        val read = raf.read(buff)
        if (read < 0) {
          Logger.error(s"Invalid ISO; attempted to read too large file $file")
          System.exit(1)
        }
        out.write(buff, 0, read)
        totalRead += read
      }
      out.close()
    }

    for (child <- dir.directoryChildren) recursivelyExtractFiles(raf, child, targetDir.resolve(child.name))
  }
}