package com.pwootage.metroidprime.formats.iso

import java.io.{ByteArrayOutputStream, DataOutputStream}

import com.pwootage.metroidprime.formats.BinarySerializable
import com.pwootage.metroidprime.formats.io.PrimeDataFile

class FST extends BinarySerializable {
  var rootDirectoryEntry: FileDirectory = null

  override def write(f: PrimeDataFile): Unit = {
    val w = new RecursiveWriter(f)
    w.writeRecur(rootDirectoryEntry, 0)
    w.writeStringTable()
    f.writePaddingTo(32)
  }

  private class RecursiveWriter(f: PrimeDataFile) {
    var offset = 0
    var stringTableBOS = new ByteArrayOutputStream()
    var stringTablePDF = new PrimeDataFile(None, Some(new DataOutputStream(stringTableBOS)))

    def writeRecur(dir: FileDirectory, parentOffset: Int): Unit = {
      val myOffset = offset

      val myEntry = new FileEntryData
      myEntry.isDirectory = true
      myEntry.filenameOffset = stringTablePDF.pos.toInt
      stringTablePDF.writeString(dir.name)
      myEntry.fileOffset = parentOffset
      myEntry.fileLength = myOffset + dir.fileChildren.length + dir.directoryChildren.length + 1
      //Sigh, this is such a hack
      if (dir == rootDirectoryEntry) myEntry.fileLength = dir.recursivelyCalculateEntryCount + 1
      f.write(myEntry)
      offset += 1

      for (child <- dir.fileChildren) {
        val entry = new FileEntryData
        entry.isDirectory = false
        entry.filenameOffset = stringTablePDF.pos.toInt
        stringTablePDF.writeString(child.name)
        entry.fileOffset = child.offset
        entry.fileLength = child.length
        f.write(entry)
        offset += 1
      }

      for (child <- dir.directoryChildren) {
        writeRecur(child, myOffset)
      }
    }

    def writeStringTable(): Unit = {
      f.writeBytes(stringTableBOS.toByteArray)
    }
  }

  override def read(f: PrimeDataFile): Unit = {
    val rde = new FileEntryData
    f.read(rde)

    val entryDatas = f.readArray(rde.numEntries - 1, () => new FileEntryData)
    val stringTable = {
      var res = Map[Int, String]()
      val maxOffset = entryDatas.map(_.filenameOffset).max
      var stringOffset = 0
      while (stringOffset < maxOffset + 1) {
        val str = f.readString()
        res = res + (stringOffset -> str)
        stringOffset += str.length + 1
      }
      res
    }

    rootDirectoryEntry = new FileDirectory(stringTable(rde.filenameOffset), Seq(), Seq())
    var directoriesByOffset = Map[Int, FileDirectory](0 -> rootDirectoryEntry)
    var currentDirectory = rootDirectoryEntry
    for (i <- entryDatas.indices) {
      val entry = entryDatas(i)
      if (entry.isDirectory) {
        currentDirectory = new FileDirectory(stringTable(entry.filenameOffset), Seq(), Seq())
        directoriesByOffset(entry.parentOffset).directoryChildren :+= currentDirectory
        directoriesByOffset += (i -> currentDirectory)
      } else {
        val newFile = new FileEntry(stringTable(entry.filenameOffset), entry.fileOffset, entry.fileLength)
        currentDirectory.fileChildren :+= newFile
      }
    }
  }
}
