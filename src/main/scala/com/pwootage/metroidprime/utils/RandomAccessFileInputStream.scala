package com.pwootage.metroidprime.utils

import java.io.{InputStream, RandomAccessFile}

class RandomAccessFileInputStream(raf: RandomAccessFile) extends InputStream {

  override def read(): Int = raf.read()

  override def read(b: Array[Byte]): Int = raf.read(b)

  override def read(b: Array[Byte], off: Int, len: Int): Int = raf.read(b, off, len)

  override def skip(n: Long): Long = raf.skipBytes(n.toInt)

  override def close(): Unit = raf.close()
}
