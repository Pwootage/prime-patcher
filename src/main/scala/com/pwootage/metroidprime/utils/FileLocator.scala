package com.pwootage.metroidprime.utils

import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.{Files, Path, Paths}
import java.util.function.BiPredicate
import java.util.stream.Collectors

import scala.collection.JavaConversions._

object FileLocator {
  def findFilesInBasePathWithExtension(baseDir: String, extension: String) = {
    val fileStream = Files.find(Paths.get(baseDir), 4, new BiPredicate[Path, BasicFileAttributes] {
      override def test(t: Path, u: BasicFileAttributes): Boolean = u.isRegularFile && t.getFileName.toString.endsWith("." + extension)
    })
    val files = fileStream.collect(Collectors.toList())
    fileStream.close()
    files.toIndexedSeq
  }
}
