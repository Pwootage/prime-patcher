package com.pwootage.metroidprime.dump

import java.io._
import java.nio.file.{Files, Path, Paths, StandardOpenOption}
import java.util.zip.{Deflater, DeflaterOutputStream, InflaterOutputStream}

import com.pwootage.metroidprime.formats.common.PrimeVersion
import com.pwootage.metroidprime.formats.io.PrimeDataFile
import com.pwootage.metroidprime.formats.iso.{FST, FileDirectory, GCIsoHeaders}
import com.pwootage.metroidprime.formats.pak.{PAKFile, Resource}
import com.pwootage.metroidprime.utils._
import org.anarres.lzo._

import scala.collection.mutable

class Patcher(targetFile: String, force: Boolean, quieter: Boolean, patchfiles: Seq[Patchfile]) {
  val patchesByFile = {
    val res = new mutable.HashMap[String, mutable.Set[(Patchfile, PatchAction)]] with mutable.MultiMap[String, (Patchfile, PatchAction)]
    val tuples = patchfiles.flatMap(patchfile =>
      patchfile.patches.map(a => (a.filename, a, patchfile))
    ).foreach(tuple => res.addBinding(tuple._1, (tuple._3, tuple._2)))
    res
  }

  def patch(fileName: String): Unit = {
    val src = Paths.get(fileName)
    val dest = Paths.get(targetFile)
    if (Files.exists(dest)) {
      if (Files.isRegularFile(dest)) {
        if (src.equals(dest)) {
          Logger.error("Can't write to the source file!")
          System.exit(1)
        } else if (!force) {
          Logger.error("Target file already exists; pass -f to force overwrite")
          System.exit(1)
        } else {
          Logger.info("Target file already exits; force specified, overwriting existing files")
        }
      } else {
        Logger.error("Target file exists and is not a regular file")
        System.exit(1)
      }
    } else {
      Files.createDirectories(dest.getParent)
    }
    val srcRaf = new RandomAccessFile(src.toFile, "r")
    val destRaf = new RandomAccessFile(dest.toFile, "rw")

    Logger.progress("Reading ISO header information...")

    val header = new GCIsoHeaders
    srcRaf.seek(0)
    PrimeDataFile(Some(srcRaf), Some(srcRaf)).read(header)

    val gameID = DataTypeConversion.intContainingCharsAsStr(header.discHeader.gameCode) + DataTypeConversion.intContainingCharsAsStr(header.discHeader.makerCode)

    Logger.info(s"Found game ID $gameID version ${header.discHeader.version} (Internal name: ${header.discHeader.name})")

    val version = if (gameID == "GM8E01") {
      Some(PrimeVersion.PRIME_1)
    } else if (gameID == "G2ME01") {
      Some(PrimeVersion.PRIME_2)
    } else {
      None
    }

    if (version.isEmpty) {
      Logger.error("Unknown game; can't patch")
      System.exit(1)
    }

    Logger.progress("Parsing file structure....")
    val fst = new FST
    srcRaf.seek(header.discHeader.fstOffset)
    PrimeDataFile(Some(srcRaf), Some(srcRaf)).read(fst)

    Logger.info(s"Found ${fst.rootDirectoryEntry.recursivelyCalculateEntryCount + 1} files and folders")

    //Now that we've loaded the existing header, we need to write it to our new file
    Logger.progressResetLine("Writing initial output header...")
    destRaf.seek(0)
    PrimeDataFile(Some(destRaf), Some(destRaf)).write(header)

    Logger.progressResetLine("Writing file structure to iso...")
    header.discHeader.fstOffset = destRaf.getFilePointer.toInt
    PrimeDataFile(Some(destRaf), Some(destRaf)).write(fst)
    header.discHeader.fstSize = destRaf.getFilePointer.toInt - header.discHeader.fstOffset
    header.discHeader.fstMaxSize = header.discHeader.fstSize

    recursivelyPatchFiles(version, srcRaf, destRaf, fst.rootDirectoryEntry)

    //Truncate rest of file
    destRaf.setLength(destRaf.getFilePointer)
    Logger.info(s"Finished writing files; final file length ${destRaf.getFilePointer}")

    val bootOffset = fst.rootDirectoryEntry.fileChildren.find(_.name == "default.dol").get.offset
    Logger.progressResetLine(s"Boot dol offset: $bootOffset")
    header.discHeader.bootDolOffset = bootOffset

    Logger.progressResetLine("Re-writing header...")
    destRaf.seek(0)
    PrimeDataFile(Some(destRaf), Some(destRaf)).write(header)

    Logger.progressResetLine("Re-writing file structure...")
    destRaf.seek(header.discHeader.fstOffset)
    PrimeDataFile(Some(destRaf), Some(destRaf)).write(fst)

    srcRaf.close()
    destRaf.close()
    Logger.success("Done")
  }

  private def recursivelyPatchFiles(version: Option[PrimeVersion], srcRaf: RandomAccessFile, destRaf: RandomAccessFile, dir: FileDirectory): Unit = {
    for (file <- dir.fileChildren) {
      val len = file.length
      Logger.progress(s"${file.name} (${DataTypeConversion.bytesToReadable(len)})")
      srcRaf.seek(file.offset)

      val fileOffset = destRaf.getFilePointer
      if (file.name.toLowerCase.endsWith(".pak")) {
        Logger.info("Found PAK file; patching it")

        srcRaf.seek(file.offset)
        repackPakFromRAFFromCurrentOffset(version.get, srcRaf, destRaf)
        srcRaf.seek(file.offset + len)
      } else {
        Logger.progressResetLine(s"Copying ${file.name}")

        if (shouldPatch(file.name)) {
          val unpatchedBytes = new ByteArrayOutputStream()
          copyBytes(new RandomAccessFileInputStream(srcRaf), len, unpatchedBytes)

          var patchedBytes = unpatchedBytes.toByteArray

          val patches = patchesByFile(file.name)

          for ((patchfile, patch) <- patches) {
            Logger.info(s"Patching ${file.name} - ${patch.description.getOrElse("")}")
            patchedBytes = applyPatch(version.get, patchfile, patch, patchedBytes)
          }

          copyBytes(new ByteArrayInputStream(patchedBytes), patchedBytes.length, new RandomAccessFileOutputStream(destRaf))

        } else {
          srcRaf.seek(file.offset)
          copyBytes(new RandomAccessFileInputStream(srcRaf), len, new RandomAccessFileOutputStream(destRaf))
        }
      }

      val fileLen = destRaf.getFilePointer - fileOffset

      file.offset = fileOffset.toInt
      file.length = fileLen.toInt
    }

    for (child <- dir.directoryChildren) recursivelyPatchFiles(version, srcRaf, destRaf, child)
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

  private def repackPakFromRAFFromCurrentOffset(primeVersion: PrimeVersion, srcRaf: RandomAccessFile, destRaf: RandomAccessFile) = {
    val srcPakStart = srcRaf.getFilePointer
    val destPakStart = destRaf.getFilePointer

    val pak = new PAKFile(primeVersion)
    new PrimeDataFile(Some(srcRaf), None).read(pak)

    var processedFiles = 0
    var patchedFiles = 0

    Logger.info(s"Named files: ${pak.namedResources.length}, Files: ${pak.resources.length}")

    //Initial write of the header; we'll re-write the header after we've calculated all the appropriate offsets
    destRaf.seek(destPakStart)
    PrimeDataFile(Some(destRaf), Some(destRaf))
      .write(pak)
      .setOffset(destRaf.getFilePointer.toInt)
      .writePaddingBytesGivenStartOffset(destPakStart, 32)

    for (resource <- pak.resources.sortBy(_.offset)) {
      val resourceName = resource.idStr
      if (!quieter) {
        Logger.progressResetLine(s"Processing $resourceName $processedFiles/${pak.resources.length}")
      }

      if (patchResourceFromPAK(primeVersion, srcRaf, destRaf, srcPakStart, destPakStart, resource)) {
        patchedFiles += 1
      }
      processedFiles += 1
    }

    val end = destRaf.getFilePointer

    Logger.progressResetLine("Rewriting PAK header...")
    destRaf.seek(destPakStart)
    new PrimeDataFile(Some(destRaf), Some(destRaf)).write(pak)
    destRaf.seek(end)
  }

  def patchResourceFromPAK(primeVersion: PrimeVersion, srcRaf: RandomAccessFile, destRaf: RandomAccessFile, srcPakStart: Long, destPakStart: Long, resource: Resource): Boolean = {
    val resourceName = resource.idStr
    srcRaf.seek(srcPakStart + resource.offset)

    if (shouldPatch(resource.idStr)) {
      actuallyPatchResource(primeVersion, srcRaf, destRaf, resource, srcPakStart, destPakStart)

      true
    } else {
      //Don't patch
      val out = new RandomAccessFileOutputStream(destRaf)
      resource.offset = (destRaf.getFilePointer - destPakStart).toInt
      copyBytes(new RandomAccessFileInputStream(srcRaf), resource.size, out)
      false
    }
  }

  def actuallyPatchResource(primeVersion: PrimeVersion, srcRaf: RandomAccessFile, destRaf: RandomAccessFile, resource: Resource, srcPakStart: Long, destPakStart: Long): Unit = {
    val unpatchedBytes = new ByteArrayOutputStream()

    readResourceToStream(primeVersion, srcRaf, resource, unpatchedBytes)

    var patchedBytes = unpatchedBytes.toByteArray

    val patches = patchesByFile(resource.idStr)

    for ((patchfile, patch) <- patches) {
      Logger.info(s"Patching ${resource.idStr} - ${patch.description.getOrElse("")}")
      patchedBytes = applyPatch(primeVersion, patchfile, patch, patchedBytes)
    }

    writeResourceToStream(primeVersion, destRaf, resource, destPakStart, patchedBytes)

  }

  def readResourceToStream(primeVersion: PrimeVersion, srcRaf: RandomAccessFile, resource: Resource, byteOut: ByteArrayOutputStream): Unit = {
    if (resource.compressed) {
      val decompressedSize = srcRaf.readInt()

      if (primeVersion == PrimeVersion.PRIME_1) {
        val resourceStart = srcRaf.getFilePointer

        val decompressedOut = new InflaterOutputStream(byteOut)
        copyBytes(new RandomAccessFileInputStream(srcRaf), resource.size - 4, decompressedOut)
        decompressedOut.flush()
        val resourceEnd = srcRaf.getFilePointer
        val bytesRead = resourceEnd - resourceStart
        if (bytesRead != resource.size - 4) {
          throw new IOException("Read incorrect number of bytes from original file")
        }
      } else if (primeVersion == PrimeVersion.PRIME_2) {
        var decompressedSoFar = 0
        while (decompressedSoFar < decompressedSize) {
          //Input
          val inBytes = new Array[Byte](srcRaf.readUnsignedShort())
          srcRaf.readFully(inBytes)
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
          byteOut.write(outBytes)
          decompressedSoFar += toRead
        }
        //Done decompressing
      } else {
        throw new Error("I did something wrong D:")
      }
    } else {
      copyBytes(new RandomAccessFileInputStream(srcRaf), resource.size, byteOut)
    }
  }

  def writeResourceToStream(primeVersion: PrimeVersion, destRaf: RandomAccessFile, resource: Resource, destPakStart: Long, data: Array[Byte]): Unit = {
    val resourceStart = destRaf.getFilePointer
    resource.offset = (resourceStart - destPakStart).toInt

    //First, write the compressed version (we'll re-write with uncompressed if it makes sense to)
    val decompressedSize = data.length
    var compressedSize: Option[Int] = None

    if (isCompressedType(decompressedSize, resource.typ)) {
      //No reason to compress things that are too small, unless they're a compressed type
      destRaf.writeInt(decompressedSize) // Write decompressed size
      val resourceInput = new ByteArrayInputStream(data)

      if (primeVersion == PrimeVersion.PRIME_1) {
        //Compress
        val compressedOut = new DeflaterOutputStream(new RandomAccessFileOutputStream(destRaf), new Deflater(9, false))
        copyBytes(resourceInput, decompressedSize, compressedOut)
        compressedOut.finish()

      } else if (primeVersion == PrimeVersion.PRIME_2) {
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
          destRaf.writeShort(outBytes.length)
          destRaf.write(outBytes)
        }
        //Done compressing
      } else {
        throw new Error("I did something wrong D:")
      }

      resourceInput.close()

      val resourceEnd = destRaf.getFilePointer
      resource.compressed = true
      resource.size = (resourceEnd - resourceStart).toInt
      compressedSize = Some[Int](resource.size)

    }

    if (compressedSize.isEmpty || compressedSize.exists(_ > decompressedSize)) {
      resource.compressed = false
      resource.size = decompressedSize

      //Write uncompressed file
      val resourceInput = new ByteArrayInputStream(data)
      destRaf.seek(resourceStart)
      copyBytes(resourceInput, decompressedSize, new RandomAccessFileOutputStream(destRaf))
      resourceInput.close()
    }

    resource.size += {
      val padStart = destRaf.getFilePointer.toInt
      val padEnd = PrimeDataFile(Some(destRaf), Some(destRaf))
        .setOffset(destRaf.getFilePointer.toInt)
        .writePaddingBytesGivenStartOffset(destPakStart, 32, 0xFF.toByte)
        .pos
      (padEnd - padStart).toInt
    }
  }

  def shouldPatch(file: String): Boolean = {
    patchesByFile.contains(file)
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

  def applyPatch(primeVersion: PrimeVersion, patchfile: Patchfile, patch: PatchAction, src: Array[Byte]): Array[Byte] = {
    patch.execute(primeVersion, patchfile.patchfileLocation.get.getParent, src)
  }
}