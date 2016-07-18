package com.pwootage.metroidprime.utils

import java.nio.{ByteBuffer, ByteOrder, IntBuffer}
import java.nio.channels.FileChannel
import java.nio.charset.StandardCharsets
import java.nio.file.Paths

import com.fasterxml.jackson.databind.util.ArrayBuilders.ByteBuilder
import com.pwootage.metroidprime.formats.iso.GCDiscHeader

object FileIdentifier {
  def isScriptLayer(bytes: Array[Byte]): Boolean = {
    val firstFourBytes = bytes.slice(0, 4)
    (firstFourBytes sameElements "SCLY".getBytes) || (firstFourBytes sameElements "SCGN".getBytes)
  }

  def isISO(file: String): Boolean = try {
    val fc = FileChannel.open(Paths.get(file))
    try {
      fc.position(0x001c)
      val buff = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN)
      fc.read(buff)
      buff.flip()
      val magic = buff.getInt
      magic == GCDiscHeader.magic
    } finally {
      fc.close()
    }
  } catch {
    case e: Throwable =>
      Console.err.println(e.toString)
      false
  }
}
