package com.pwootage.metroidprime.dump

import java.io.{DataInputStream, RandomAccessFile}
import java.nio.file.{Files, Paths}

import com.pwootage.metroidprime.formats.io.PrimeDataFile
import com.pwootage.metroidprime.formats.iso.{FST, GCIsoHeaders}
import com.pwootage.metroidprime.utils.{ByteDiffer, DataTypeConversion}
import com.sun.deploy.util.SyncFileAccess.RandomAccessFileLock

object Extractor {
  def extractIso(fileName: String) = {
    val file = Paths.get(fileName)
    val raf = new RandomAccessFile(file.toFile, "rw")

    println("Reading ISO header information...")

    val header = new GCIsoHeaders
    raf.seek(0)
    PrimeDataFile(Some(raf), Some(raf)).read(header)

    val gameID = DataTypeConversion.intAsStr(header.discHeader.gameCode) + DataTypeConversion.intAsStr(header.discHeader.makerCode)

    println(s"Found game ID $gameID version ${header.discHeader.version}")
    println(s"(Internal name: ${header.discHeader.name})")

    println("Parsing file structure....")
    val fst = new FST
    raf.seek(header.discHeader.fstOffset)
    PrimeDataFile(Some(raf), Some(raf)).read(fst)

    println(s"Found ${fst.rootDirectoryEntry.recursivelyCalculateEntryCount + 1} files and folders")

    raf.close()

    println("Done")
  }
}