package com.pwootage.metroidprime.dump

import java.io._
import java.nio.file.{Files, Path, Paths, StandardOpenOption}
import java.util.stream.Collectors
import java.util.zip.{Deflater, DeflaterInputStream, DeflaterOutputStream, InflaterOutputStream}

import scala.collection.JavaConversions._
import com.pwootage.metroidprime.formats.common.PrimeVersion
import com.pwootage.metroidprime.formats.io.PrimeDataFile
import com.pwootage.metroidprime.formats.iso.{FST, FileDirectory, FileEntry, GCIsoHeaders}
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

    Logger.progressResetLine("Writing initial headers...")
    targetRaf.seek(0)
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

    val fst = new FST
    fst.rootDirectoryEntry = parseRealFilesystemIntoFST("<root>", src)

    Logger.info(s"Found ${fst.rootDirectoryEntry.recursivelyCalculateEntryCount + 1} files and directories to put into ISO")

    Logger.progressResetLine("Writing file structure to iso...")
    header.discHeader.fstOffset = targetRaf.getFilePointer.toInt
    PrimeDataFile(Some(targetRaf), Some(targetRaf)).write(fst)
    header.discHeader.fstSize = targetRaf.getFilePointer.toInt - header.discHeader.fstOffset
    header.discHeader.fstMaxSize = header.discHeader.fstSize

    recursivelyWriteFiles(src, fst.rootDirectoryEntry, targetRaf)

    //Truncate rest of file
    targetRaf.setLength(targetRaf.getFilePointer)
    Logger.info(s"Finished writing files; final file length ${targetRaf.getFilePointer}")

    val bootOffset = fst.rootDirectoryEntry.fileChildren.find(_.name == "default.dol").get.offset
    Logger.progressResetLine(s"Boot dol offset: $bootOffset")
    header.discHeader.bootDolOffset = bootOffset

    Logger.progressResetLine("Re-writing header...")
    targetRaf.seek(0)
    PrimeDataFile(Some(targetRaf), Some(targetRaf)).write(header)

    Logger.progressResetLine("Re-writing file structure...")
    targetRaf.seek(header.discHeader.fstOffset)
    PrimeDataFile(Some(targetRaf), Some(targetRaf)).write(fst)

    Logger.success("Done")
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
    PrimeDataFile(Some(targetRaf), Some(targetRaf))
      .write(realList)
      .setOffset(targetRaf.getFilePointer.toInt)
      .writePaddingBytesGivenStartOffset(pakStart, 32)

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

      if (isCompressedType(decompressedSize, resource.typ)) {
        //No reason to compress things that are too small, unless they're a compressed type
        targetRaf.writeInt(decompressedSize) // Write decompressed size
        val resourceInput = Files.newInputStream(resourcePath, StandardOpenOption.READ)

        if (realList.primeVersion == PrimeVersion.PRIME_1) {
          //Compress
          val compressedOut = new DeflaterOutputStream(new RandomAccessFileOutputStream(targetRaf), new Deflater(9, false))
          copyBytes(resourceInput, decompressedSize, compressedOut)
          compressedOut.finish()

        } else if (realList.primeVersion == PrimeVersion.PRIME_2) {
          var compressedSoFar = 0
          while (compressedSoFar < decompressedSize) {
            //Setup
            val compressor = LzoLibrary.getInstance().newCompressor(LzoAlgorithm.LZO1X, LzoConstraint.COMPRESSION)
            val toCompress = Math.min(0x4000, decompressedSize - compressedSoFar)
            val inBytes = new ByteArrayOutputStream(toCompress)
            copyBytes(resourceInput, toCompress, inBytes)

            //Compress
            val outBuff = new Array[Byte](0x8000)
            val outLen = new lzo_uintp
            val code = compressor.compress(inBytes.toByteArray, 0, toCompress, outBuff, 0, outLen)
            if (code != LzoTransformer.LZO_E_OK) {
              throw new IOException(compressor.toErrorString(code))
            }

            //Output
            targetRaf.writeShort(outLen.value)
            copyBytes(new ByteArrayInputStream(outBuff), outLen.value, new RandomAccessFileOutputStream(targetRaf))
            compressedSoFar += toCompress
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

      resource.size += {
        val padStart = targetRaf.getFilePointer.toInt
        val padEnd = PrimeDataFile(Some(targetRaf), Some(targetRaf))
          .setOffset(targetRaf.getFilePointer.toInt)
          .writePaddingBytesGivenStartOffset(pakStart, 32, 0xFF.toByte)
          .pos
        (padEnd - padStart).toInt
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

  private def isCompressedType(size: Int, typ: Int) = DataTypeConversion.intContainingCharsAsStr(typ) match {
    case "TXTR" => true
    case "CMDL" => true
    case "CSKR" => true
    case "ANCS" => true
    case "ANIM" => true
    case "FONT" => true
    case "PART" => true
    case "ELSC" => true
    case "SWHC" => true
    case "WPSC" => true
    case "DPSC" => true
    case "CRSC" => true
    case _ => false
  }

  private def isIgnored(file: Path) = {
    if (file.getFileName.toString == "info.json") {
      true
    } else if (file.getFileName.toString == "list.json") {
      true
    } else if (isPakDirectory(file)) {
      true
    } else {
      false
    }
  }

  private def isPakDirectory(file: Path): Boolean = {
    Files.isDirectory(file) && Files.exists(file.resolve("list.json"))
  }

  private def parseRealFilesystemIntoFST(name: String, src: Path): FileDirectory = {
    val children = {
      val stream = Files.list(src)
      val res = stream.iterator().toIndexedSeq
      stream.close()
      res
    }

    val fileChildren = (for (child <- children) yield {
      if (Files.isRegularFile(child) && !isIgnored(child)) {
        Some(FileEntry(child.getFileName.toString, 0, 0))
      } else if (isPakDirectory(child)) {
        Some(FileEntry(child.getFileName.toString.replace("-", "."), 0, 0))
      } else {
        None
      }
    }).flatten

    val directoryChildren = for (child <- children if Files.isDirectory(child) && !isIgnored(child)) yield {
      parseRealFilesystemIntoFST(child.getFileName.toString, child)
    }

    FileDirectory(name, fileChildren, directoryChildren)
  }

  def recursivelyWriteFiles(src: Path, dir: FileDirectory, targetRaf: RandomAccessFile): Unit = {
    for (file <- dir.fileChildren) {
      new PrimeDataFile(Some(targetRaf), Some(targetRaf))
        .setOffset(targetRaf.getFilePointer.toInt)
        .writePaddingTo(2048)

      if (file.name.toLowerCase.endsWith(".pak") && isPakDirectory(src.resolve(file.name.replace('.', '-')))) {
        Logger.info("Found PAK; packing in-line")
        file.offset = targetRaf.getFilePointer.toInt

        repackPak(src.resolve(file.name.replace('.', '-')), targetRaf)

        file.length = targetRaf.getFilePointer.toInt - file.offset
      } else {
        val srcFile = src.resolve(file.name)

        file.offset = targetRaf.getFilePointer.toInt
        file.length = Files.size(srcFile).toInt

        Logger.progressResetLine(s"Writing ${file.name} @ ${file.offset} (${file.length} bytes)")
        val fin = Files.newInputStream(srcFile)
        copyBytes(fin, file.length, new RandomAccessFileOutputStream(targetRaf))
      }

      new PrimeDataFile(Some(targetRaf), Some(targetRaf))
        .setOffset(targetRaf.getFilePointer.toInt)
        .writePaddingTo(2048)
    }

    for (dir <- dir.directoryChildren) {
      recursivelyWriteFiles(src.resolve(dir.name), dir, targetRaf)
    }
  }
}