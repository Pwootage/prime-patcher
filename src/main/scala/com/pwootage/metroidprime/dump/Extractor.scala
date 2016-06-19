package com.pwootage.metroidprime.dump

import java.io._
import java.nio.file.{Files, Path, Paths, StandardOpenOption}
import java.util.zip.InflaterOutputStream

import com.pwootage.metroidprime.formats.common.PrimeVersion
import com.pwootage.metroidprime.formats.io.PrimeDataFile
import com.pwootage.metroidprime.formats.iso.{FST, FileDirectory, GCIsoHeaders}
import com.pwootage.metroidprime.formats.pak.PAKFile
import com.pwootage.metroidprime.utils.{DataTypeConversion, Logger, PrimeJacksonMapper}
import org.anarres.lzo._

class Extractor(targetDirectory: String, force: Boolean, extractPaks: Boolean, quieter: Boolean) {

  def extractIso(fileName: String): Unit = {
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

    val version = if (gameID == "GM8E01") {
      Some(PrimeVersion.PRIME_1)
    } else if (gameID == "G2ME01") {
      Some(PrimeVersion.PRIME_2)
    } else {
      None
    }

    if (version.isEmpty && extractPaks) {
      Logger.error("Attempting to extract PAKs of a non-Prime-1 or non-Prime-2 game; aborting")
      System.exit(1)
    }

    Logger.progress("Parsing file structure....")
    val fst = new FST
    raf.seek(header.discHeader.fstOffset)
    PrimeDataFile(Some(raf), Some(raf)).read(fst)

    Logger.info(s"Found ${fst.rootDirectoryEntry.recursivelyCalculateEntryCount + 1} files and folders")

    //Write some header info to files so we can parse it again later
    Files.write(target.resolve("info.json"), PrimeJacksonMapper.pretty.writeValueAsBytes(header))
    recursivelyExtractFiles(version, raf, fst.rootDirectoryEntry, target)

    raf.close()

    Logger.success("Done")
  }

  def extractPak(fileName: String): Unit = {
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
    raf.seek(0)

    Logger.info("Determining Prime version...")

    val pak = new PAKFile(PrimeVersion.PRIME_1) //Doesn't matter, we're just looking for a compressed file
    pak.read(new PrimeDataFile(Some(raf), None))

    val compressedResource = pak.resources.find(_.compressed)

    val ver = compressedResource match {
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

    Logger.info(s"Detected ${ver.prettyName}")
    raf.seek(0)
    extractPakFromRAFFromCurrentOffset(ver, raf, target)

    Logger.success("Done")
  }

  private def recursivelyExtractFiles(version: Option[PrimeVersion], raf: RandomAccessFile, dir: FileDirectory, targetDir: Path): Unit = {
    Files.createDirectories(targetDir)
    for (file <- dir.fileChildren) {
      val len = file.length
      Logger.progress(s"${file.name} (${DataTypeConversion.bytesToReadable(len)})")
      raf.seek(file.offset)

      if (file.name.toLowerCase.endsWith(".pak") && extractPaks && shouldExtract(file.name)) {
        Logger.info("Found PAK file; extracting it")
        raf.seek(file.offset)
        extractPakFromRAFFromCurrentOffset(version.get, raf, targetDir.resolve(file.name.replace('.', '-')))
        raf.seek(file.offset + len)
      } else {
        val out = Files.newOutputStream(targetDir.resolve(file.name), StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)
        copyBytes(raf, len, out)
        out.close()
      }
    }

    for (child <- dir.directoryChildren) recursivelyExtractFiles(version, raf, child, targetDir.resolve(child.name))
  }

  private def copyBytes(in: RandomAccessFile, len: Int, out: OutputStream): Unit = {
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

  def shouldExtract(name: String): Boolean = {
//    name.toLowerCase.contains("metroid")
    name.toLowerCase.contains("metroid4")
//    true
  }


  private def extractPakFromRAFFromCurrentOffset(primeVersion: PrimeVersion, raf: RandomAccessFile, targetDir: Path) = {
    Files.createDirectories(targetDir)

    val offset = raf.getFilePointer
    val pak = new PAKFile(primeVersion)
    pak.read(new PrimeDataFile(Some(raf), None))
    Files.write(targetDir.resolve("list.json"), PrimeJacksonMapper.pretty.writeValueAsBytes(pak.toBasicResourceList))

    val uniqueFiles = pak.resources.map(_.idStr).toSet
    var alreadyExtractedFiles = Set[String]()

    Logger.info(s"Named files: ${pak.namedResources.length}, Files: ${pak.resources.length}, Unique Files: ${uniqueFiles.size}")

    for (resource <- pak.resources.sortBy(_.offset)) {
      val resourceName = resource.idStr
      if (!alreadyExtractedFiles.contains(resourceName)) {
        alreadyExtractedFiles += resourceName
        if (quieter) {
          if (alreadyExtractedFiles.size % 500 == 0) {
            Logger.progressResetLine(s"Extracted ${alreadyExtractedFiles.size}/${uniqueFiles.size}")
          }
        } else {
          Logger.progressResetLine(s"Extracting $resourceName (${alreadyExtractedFiles.size}/${uniqueFiles.size})")
        }

        raf.seek(offset + resource.offset)
        val out = Files.newOutputStream(targetDir.resolve(resourceName), StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)
        if (resource.compressed) {
          val decompressedSize = raf.readInt()

          if (primeVersion == PrimeVersion.PRIME_1) {
            val resourceStart = raf.getFilePointer
            val decompressedOut = new InflaterOutputStream(out)
            copyBytes(raf, resource.size - 4, decompressedOut)
            decompressedOut.flush()
            val resourceEnd = raf.getFilePointer
            val bytesRead = resourceEnd - resourceStart
            if (bytesRead != resource.size - 4) {
              throw new IOException("Read incorrect number of bytes from original file")
            }
          } else if (primeVersion == PrimeVersion.PRIME_2) {
            var decompressedSoFar = 0
            while (decompressedSoFar < decompressedSize) {
              //Input
              val inBytes = new Array[Byte](raf.readUnsignedShort())
              raf.readFully(inBytes)
              val toRead = Math.min(0x4000, decompressedSize - decompressedSoFar)
              val outBytes = new Array[Byte](toRead)

              //Decompress/verify
              val decompressor = LzoLibrary.getInstance().newDecompressor(LzoAlgorithm.LZO1X, LzoConstraint.COMPRESSION)
              val outLen = new lzo_uintp
              val code = decompressor.decompress(inBytes, 0, inBytes.length, outBytes, 0, outLen)
              if (code != LzoTransformer.LZO_E_OK) {
                throw new IOException(decompressor.toErrorString(code))
              }
              if (outLen.value != toRead) {
                throw new IOException(s"Read incorrect number of bytes: $outLen")
              }

              //Output
              out.write(outBytes)
              decompressedSoFar += toRead
            }
            //Done decompressing
          } else {
            throw new Error("I did something wrong D:")
          }
        } else {
          copyBytes(raf, resource.size, out)
        }
        out.close()
      }
    }
  }
}