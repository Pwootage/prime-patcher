package com.pwootage.metroidprime.dump

import java.io._
import java.nio.file.{Files, Path, Paths, StandardOpenOption}
import java.util.zip.{Deflater, DeflaterInputStream, DeflaterOutputStream, InflaterOutputStream}

import com.pwootage.metroidprime.formats.common.PrimeVersion
import com.pwootage.metroidprime.formats.io.PrimeDataFile
import com.pwootage.metroidprime.formats.iso.{FST, FileDirectory, GCIsoHeaders}
import com.pwootage.metroidprime.formats.pak.{BasicResourceList, PAKFile}
import com.pwootage.metroidprime.utils.{DataTypeConversion, Logger, PrimeJacksonMapper, RandomAccessFileOutputStream}
import org.anarres.lzo._

class Repacker(targetFile: String, force: Boolean, quieter: Boolean) {
  def repack(srcDir: String): Unit = {
    val src = Paths.get(srcDir)
    val target = Paths.get(targetFile)
    if (Files.exists(target)) {
      if (Files.isRegularFile(target)) {
        if (!force) {
          Logger.error("Target file already exists; pass -f to force overwrite")
          System.exit(1)
        } else {
          Logger.info("Target file already exits; force specified, overwriting existing files")
        }
      } else {
        Logger.error("Target exists and is not a regular file")
        System.exit(1)
      }
    }

    Logger.progress("Checking if this is an ISO or a PAK...")

    val infoJson = src.resolve("info.json")
    val listJson = src.resolve("list.json")
    if (Files.isRegularFile(infoJson)) {
      Logger.info("Repacking ISO and any necessary PAK files")
      repackIso(src, target)
    } else if (Files.isRegularFile(listJson)) {
      Logger.info("Repacking PAK file")
      val targetRaf = new RandomAccessFile(target.toFile, "rw")
      repackPak(src, targetRaf)
      new PrimeDataFile(Some(targetRaf), Some(targetRaf)).writePaddingTo(32)
      targetRaf.setLength(targetRaf.getFilePointer)
    } else {
      Logger.error("Target directory does not appear to be an ISO (info.json) or a PAK (lists.json)")
      System.exit(1)
    }
  }

  def repackIso(src: Path, target: Path): Unit = {
    val targetRaf = new RandomAccessFile(target.toFile, "rw")

    val infoJson = src.resolve("info.json")
    val header = PrimeJacksonMapper.mapper.readValue(Files.readAllBytes(infoJson), classOf[GCIsoHeaders])
    targetRaf.seek(0)
    //Initial header write; note we'll write it a second time after we've substituted in correct offsets
    PrimeDataFile(Some(targetRaf), Some(targetRaf)).write(header)

    val gameID = DataTypeConversion.intContainingCharsAsStr(header.discHeader.gameCode) + DataTypeConversion.intContainingCharsAsStr(header.discHeader.makerCode)

    Logger.info(s"Found game ID $gameID version ${header.discHeader.version} (Internal name: ${header.discHeader.name})")

    val version = if (gameID == "GM8E01") {
      Some(PrimeVersion.PRIME_1)
    } else if (gameID == "G2ME01") {
      Some(PrimeVersion.PRIME_2)
    } else {
      None
    }

    //    Logger.progress("Parsing file structure....")
    //    val fst = new FST
    //    raf.seek(header.discHeader.fstOffset)
    //    PrimeDataFile(Some(raf), Some(raf)).read(fst)
    //
    //    Logger.info(s"Found ${fst.rootDirectoryEntry.recursivelyCalculateEntryCount + 1} files and folders")
    //
    //    //Write some header info to files so we can parse it again later
    //    Files.write(target.resolve("info.json"), PrimeJacksonMapper.pretty.writeValueAsBytes(header))
    //    recursivelyExtractFiles(version, raf, fst.rootDirectoryEntry, target)
    //
    //    raf.close()
    //
    //    Logger.success("Done")
  }

  def repackPak(src: Path, targetRaf: RandomAccessFile): Unit = {
    val pakStart = targetRaf.getFilePointer

    val listsJson = src.resolve("list.json")
    val basicList = PrimeJacksonMapper.mapper.readValue(Files.readAllBytes(listsJson), classOf[BasicResourceList])
    val realList = new PAKFile(basicList.primeVersion)
    realList.fromBasicResourceList(basicList)
    Logger.progressResetLine(s"Writing initial header...")
    //Initial write of the header; we'll re-write the header after we've calculated all the appropriate offsets
    targetRaf.seek(pakStart)
    PrimeDataFile(Some(targetRaf), Some(targetRaf)).write(realList)

    var extractedFiles = 0
    for (resource <- realList.resources) {
      val resourceName = resource.idStr
      extractedFiles += 1
      if (quieter) {
        if (extractedFiles % 500 == 0) {
          Logger.progressResetLine(s"Repacking $extractedFiles/${realList.resources.length}")
        }
      } else {
        Logger.progressResetLine(s"Repacking $resourceName ($extractedFiles/${realList.resources.length})")
      }

      val resourceStart = targetRaf.getFilePointer
      resource.offset = (resourceStart - pakStart).toInt
      val resourcePath = src.resolve(resourceName)

      //First, write the compressed version (we'll re-write with uncompressed if it makes sense to)
      val decompressedSize = Files.size(resourcePath).toInt
      var compressedSize: Option[Int] = None

      if (decompressedSize > 0x400) { //No reason to compress things that are too small
        targetRaf.writeInt(decompressedSize) // Write decompressed size
        val resourceInput = Files.newInputStream(resourcePath, StandardOpenOption.READ)

        if (realList.primeVersion == PrimeVersion.PRIME_1) {
          //Manually add header
//          targetRaf.writeShort(0x78DA)
          //Compress
          val compressedOut = new DeflaterOutputStream(new RandomAccessFileOutputStream(targetRaf), new Deflater(9, false))
          copyBytes(resourceInput, decompressedSize, compressedOut)
          compressedOut.finish()

        } else if (realList.primeVersion == PrimeVersion.PRIME_2) {
          var compressedSoFar = 0
          while (compressedSoFar < decompressedSize) {
            //Setup
            val compressor = LzoLibrary.getInstance().newCompressor(LzoAlgorithm.LZO1X, LzoConstraint.COMPRESSION)
            val outByteStream = new ByteArrayOutputStream()
            val compressedOuputStream = new LzoOutputStream(outByteStream, compressor)
            val toCompress = Math.min(0x4000, decompressedSize - compressedSoFar)

            //Compress
            copyBytes(resourceInput, toCompress, compressedOuputStream)
            compressedOuputStream.flush()
            compressedSoFar += toCompress

            //Output
            val outBytes = outByteStream.toByteArray
            targetRaf.writeShort(outBytes.length)
            targetRaf.write(outBytes)
          }
          //Done compressing
        } else {
          throw new Error("I did something wrong D:")
        }

        resourceInput.close()

        val resourceEnd = targetRaf.getFilePointer
        resource.compressed = true
        resource.size = (resourceEnd - resourceStart).toInt
        compressedSize = Some[Int](resource.size)

      }

      if (compressedSize.isEmpty || compressedSize.exists(_ > decompressedSize)) {
        resource.compressed = false
        resource.size = decompressedSize
        //Write uncompressed file
        targetRaf.seek(resourceStart)
        val resourceInput = Files.newInputStream(resourcePath, StandardOpenOption.READ)
        copyBytes(resourceInput, decompressedSize, new RandomAccessFileOutputStream(targetRaf))
        resourceInput.close()
      }

      //Resource processing end
    }

    //Re-write header
    val pakEnd = targetRaf.getFilePointer

    Logger.progressResetLine(s"Re-writing corrected header...")
    targetRaf.seek(pakStart)
    PrimeDataFile(Some(targetRaf), Some(targetRaf)).write(realList)

    targetRaf.seek(pakEnd)
    Logger.info("Finished packing PAK.")
  }

  private def copyBytes(in: InputStream, len: Int, out: OutputStream): Unit = {
    val buff = new Array[Byte](16 * 1026) //16k blocks
    var totalRead = 0
    while (totalRead < len) {
      val toRead = Math.min(buff.length, len - totalRead)
      val read = in.read(buff, 0, toRead)
      if (read < 0) {
        throw new IOException("Attempt to read too many bytes")
      }
      out.write(buff, 0, read)
      totalRead += read
    }
  }
}