package com.pwootage.metroidprime.utils

import java.io.{ByteArrayInputStream, _}

import org.anarres.lzo._

object IOUtils {
  def compressLZOSegmentedStream(out: OutputStream, decompressedSize: Int, resourceInput: InputStream, negativeUncomprssedBlocks: Boolean = false): Unit = {
    val dout = new DataOutputStream(out)
    var compressedSoFar = 0
    while (compressedSoFar < decompressedSize) {
      //Setup
//      val compressor = LzoLibrary.getInstance().newCompressor(LzoAlgorithm.LZO1X, LzoConstraint.COMPRESSION)
      val compressor = new LzoCompressor1x_999(8)
      val toCompress = Math.min(0x4000, decompressedSize - compressedSoFar)
      val inBytesStream = new ByteArrayOutputStream(toCompress)
      copyBytes(resourceInput, toCompress, inBytesStream)
      val inBytes = inBytesStream.toByteArray

      //Compress
      val outBuff = new Array[Byte](0x8000)
      val outLen = new lzo_uintp
      val code = compressor.compress(inBytes, 0, toCompress, outBuff, 0, outLen)
      if (code != LzoTransformer.LZO_E_OK) {
        throw new IOException(compressor.toErrorString(code))
      }

      //Output
      if (outLen.value >= toCompress && negativeUncomprssedBlocks) {
        dout.writeShort(-toCompress)
        copyBytes(new ByteArrayInputStream(inBytes), toCompress, dout)
      } else {
        dout.writeShort(outLen.value)
        copyBytes(new ByteArrayInputStream(outBuff), outLen.value, dout)
      }
      compressedSoFar += toCompress
    }
  }

  def decompressSegmentedLZOStream(resourceInput: InputStream, out: OutputStream, decompressedSize: Int): Unit = {
    val din = new DataInputStream(resourceInput)
    var decompressedSoFar = 0
    while (decompressedSoFar < decompressedSize) {
      val size = din.readShort()
      if (size < 0) {
        copyBytes(din, -size, out)
        decompressedSoFar += -size
      } else {
        //Input
        val inBytes = new Array[Byte](size)
        din.readFully(inBytes)
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
        decompressedSoFar += outLen.value
      }
    }
  }

  def copyBytes(in: InputStream, len: Int, out: OutputStream): Unit = {
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

  def copyBytes(in: RandomAccessFile, len: Int, out: OutputStream): Unit = {
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
