package com.pwootage.metroidprime.formats.mrea

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, DataInputStream, IOException}
import java.util

import com.pwootage.metroidprime.formats.BinarySerializable
import com.pwootage.metroidprime.formats.common.PrimeVersion
import com.pwootage.metroidprime.formats.io.PrimeDataFile
import com.pwootage.metroidprime.formats.mrea.collision.Collision
import com.pwootage.metroidprime.formats.scly.SCLY
import com.pwootage.metroidprime.utils.{FileIdentifier, IOUtils}

object MREA {
  val MAGIC = 0xDEADBEEF
  val VERSION_PRIME_1 = 0xF
  val VERSION_PRIME_2 = 0x19

  val MAX_DECOMPRESSED_SIZE = 0x20000
}

class MREA extends BinarySerializable {
  var magic: Int = -1
  var version: Int = -1
  val transform = new Array[Float](12)
  var meshCount: Int = -1
  var scriptLayerCount: Int = -1
  var sectionCount: Int = -1
  var geometrySection: Int = -1
  var SCLYSection: Int = -1
  var generatedSCLYSection: Int = -1
  var collisionSection: Int = -1
  var unknownSection1: Int = -1
  var lightSection: Int = -1
  var VISISection: Int = -1
  var PATHSection: Int = -1
  var AROTsection: Int = -1
  var unknownSection2: Int = -1
  var portalAreaSection: Int = -1
  var staticGeomSection: Int = -1

  var rawSections = Array[Array[Byte]]()

  override def write(f: PrimeDataFile): Unit = {
    f.write32(magic)
    f.write32(version)
    f.writeArray(transform, _.writeFloat)
    f.write32(meshCount)
    if (version == MREA.VERSION_PRIME_2) {
      f.write32(scriptLayerCount)
    }
    f.write32(sectionCount)
    f.write32(geometrySection)
    f.write32(SCLYSection)
    if (version == MREA.VERSION_PRIME_2) {
      f.write32(generatedSCLYSection)
    }
    f.write32(collisionSection)
    f.write32(unknownSection1)
    f.write32(lightSection)
    f.write32(VISISection)
    f.write32(PATHSection)

    if (version == MREA.VERSION_PRIME_1) {
      f.write32(AROTsection)
      f.writeArray(rawSections.map(_.length), _.write32)
      f.writePaddingTo(32)

      for (section <- rawSections) {
        f.writeBytes(section)
        f.writePaddingTo(32)
      }
    }

    if (version == MREA.VERSION_PRIME_2) {
      f.write32(unknownSection2)
      f.write32(portalAreaSection)
      f.write32(staticGeomSection)

      //Figure out how we are going to compress our blocks
      var compressedBlockHeaders = Seq[MREACompressedBlockHeader]()
      var currentSection = 0
      while (currentSection < sectionCount) {
        val header = new MREACompressedBlockHeader
        if (FileIdentifier.isScriptLayer(rawSections(currentSection))) {
          header.dataSectionCount = 1
          header.uncompressedSize = rawSections(currentSection).length
          currentSection += 1
        } else {
          header.dataSectionCount = 0
          do {
            header.dataSectionCount += 1
            header.uncompressedSize += rawSections(currentSection).length
            currentSection += 1
          } while (
            currentSection < sectionCount && rawSections(currentSection).length + header.uncompressedSize < 0x20000
              && !FileIdentifier.isScriptLayer(rawSections(currentSection))
          )
        }
        compressedBlockHeaders = compressedBlockHeaders ++ Seq(header)
      }

      val allCompressedBlocksOutputStream = new ByteArrayOutputStream()

      currentSection = 0
      for (header <- compressedBlockHeaders) {
        val blockUncompressedBytesStream = new ByteArrayOutputStream()
        header.uncompressedSize = 0
        for (_ <- 0 until header.dataSectionCount) {
          header.uncompressedSize += rawSections(currentSection).length
          IOUtils.copyBytes(new ByteArrayInputStream(rawSections(currentSection)), rawSections(currentSection).length, blockUncompressedBytesStream)
          currentSection += 1
        }
        val blockUncompressedBytes = blockUncompressedBytesStream.toByteArray
        val blockCompressedBytesStream = new ByteArrayOutputStream()
        IOUtils.compressLZOSegmentedStream(blockCompressedBytesStream, header.uncompressedSize, new ByteArrayInputStream(blockUncompressedBytes), negativeUncomprssedBlocks = true)
        val blockCompressedBytes = blockCompressedBytesStream.toByteArray

        if (blockCompressedBytes.length >= blockUncompressedBytes.length /*|| blockUncompressedBytes.length < 0x400*/) {
          header.compressedSize = 0
          header.bufferSize = header.uncompressedSize

          val padding = {
            val mod = header.uncompressedSize % 32
            if (mod == 0) 0 else 32 - mod
          }
          allCompressedBlocksOutputStream.write(new Array[Byte](padding))


          IOUtils.copyBytes(new ByteArrayInputStream(blockUncompressedBytes), blockUncompressedBytes.length, allCompressedBlocksOutputStream)
        } else {
          header.compressedSize = blockCompressedBytes.length
          header.bufferSize = header.uncompressedSize + 0x120

          val padding = {
            val mod = header.compressedSize % 32
            if (mod == 0) 0 else 32 - mod
          }
          allCompressedBlocksOutputStream.write(new Array[Byte](padding))

          IOUtils.copyBytes(new ByteArrayInputStream(blockCompressedBytes), blockCompressedBytes.length, allCompressedBlocksOutputStream)
        }
      }

      if (currentSection != sectionCount) {
        throw new IOException(s"Failed to write enough sections $currentSection (expected $sectionCount)")
      }

      f.write32(compressedBlockHeaders.size)
      f.write32(0).write32(0).write32(0) //padding

      f.writeArray(rawSections.map(_.length), _.write32)
      f.writePaddingTo(32)

      f.writeArray(compressedBlockHeaders.toArray)
      f.writePaddingTo(32)

      f.writeBytes(allCompressedBlocksOutputStream.toByteArray)

      //Done with Prime 2 specifics
    }
  }

  override def read(f: PrimeDataFile): Unit = {
    magic = f.read32()
    version = f.read32()
    if (magic != MREA.MAGIC) {
      throw new IllegalArgumentException(s"Invalid magic: ${magic.toHexString}")
    }
    if (!(version == MREA.VERSION_PRIME_1 || version == MREA.VERSION_PRIME_2)) {
      throw new IllegalArgumentException(s"Invalid version: ${version.toHexString}")
    }
    f.readArray(transform, _.readFloat)
    meshCount = f.read32()
    if (version == MREA.VERSION_PRIME_2) {
      scriptLayerCount = f.read32()
    }
    sectionCount = f.read32()
    geometrySection = f.read32()
    SCLYSection = f.read32()
    if (version == MREA.VERSION_PRIME_2) {
      generatedSCLYSection = f.read32()
    }
    collisionSection = f.read32()
    unknownSection1 = f.read32()
    lightSection = f.read32()
    VISISection = f.read32()
    PATHSection = f.read32()

    if (version == MREA.VERSION_PRIME_1) {
      AROTsection = f.read32()

      val sectionSizes = f.readArrayWithCount(sectionCount, _.read32)
      f.readPaddingTo(32)

      rawSections = new Array(sectionCount)
      for (i <- rawSections.indices) {
        rawSections(i) = f.readBytes(sectionSizes(i))
        f.readPaddingTo(32)
      }
    }

    if (version == MREA.VERSION_PRIME_2) {
      unknownSection2 = f.read32()
      portalAreaSection = f.read32()
      staticGeomSection = f.read32()
      val compressedBlockCount = f.read32()
      f.read32() //padding
      f.read32()
      f.read32()

      val sectionSizes = f.readArrayWithCount(sectionCount, _.read32)
      f.readPaddingTo(32)
      rawSections = new Array(sectionCount)

      //Decompress the compressed blocks
      println("test")
      val compressedBlockHeaders = f.readArray(compressedBlockCount, () => new MREACompressedBlockHeader)

      f.readPaddingTo(32)

      var currentSection = 0
      for (header <- compressedBlockHeaders) {
        var toRead = header.compressedSize
        if (toRead == 0) toRead = header.uncompressedSize

        val padding = {
          val mod = toRead % 32
          if (mod == 0) 0 else 32 - mod
        }
        f.readBytes(padding)

        val sectionStart = f.pos
        val unprocessedBytes = f.readBytes(toRead)
        val decompressedByteStream = new ByteArrayOutputStream()

        if (header.compressedSize == 0) {
          IOUtils.copyBytes(new ByteArrayInputStream(unprocessedBytes), header.uncompressedSize, decompressedByteStream)
        } else {
          IOUtils.decompressSegmentedLZOStream(new ByteArrayInputStream(unprocessedBytes), decompressedByteStream, header.uncompressedSize)
        }

        val decompressedBytes = decompressedByteStream.toByteArray
        if (decompressedBytes.length != header.uncompressedSize) {
          throw new IOException(s"Decompressed block is wrong size ${decompressedBytes.length} (expected ${header.uncompressedSize})")
        }
        var currentOffset = 0
        for (blockSection <- 0 until header.dataSectionCount) {
          val currentSectionSize = sectionSizes(currentSection)
          rawSections(currentSection) = decompressedBytes.slice(currentOffset, currentOffset + currentSectionSize)
          currentOffset += currentSectionSize
          currentSection += 1
        }
        if (currentOffset != header.uncompressedSize) {
          throw new IOException(s"Decompressed did not get completely used $currentOffset (expected ${header.uncompressedSize})")
        }
      }

      if (currentSection != sectionCount) {
        throw new IOException(s"Failed to read enough sections $currentSection (expected $sectionCount)")
      }

      //Done with Prime 2 specifics
    }
  }

  def parseCollision = {
    val res = new Collision
    res.read(rawSections(collisionSection))
    res
  }

  def parseSCLY = {
    val res = new SCLY
    res.read(rawSections(SCLYSection))
    res
  }

  def setSCLY(scly: SCLY): Unit = {
    rawSections(SCLYSection) = scly.toByteArray
  }

  def primeVersion = version match {
    case MREA.VERSION_PRIME_1 => PrimeVersion.PRIME_1
    case MREA.VERSION_PRIME_2 => PrimeVersion.PRIME_2
  }
}
