package com.pwootage.metroidprime.utils

import java.io.{OutputStream, RandomAccessFile}

class RandomAccessFileOutputStream(raf: RandomAccessFile) extends OutputStream {
  override def write(b: Int): Unit = raf.write(b)

  override def write(b: Array[Byte]): Unit = raf.write(b)

  override def write(b: Array[Byte], off: Int, len: Int): Unit = raf.write(b, off, len)

  override def close(): Unit = raf.close()
}
