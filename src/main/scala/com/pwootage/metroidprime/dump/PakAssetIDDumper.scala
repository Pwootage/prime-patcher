package com.pwootage.metroidprime.dump

import java.io.DataInputStream
import java.nio.file.{Files, Path, Paths}

import com.pwootage.metroidprime.formats.common.PrimeVersion
import com.pwootage.metroidprime.formats.io.PrimeDataFile
import com.pwootage.metroidprime.formats.mlvl.MLVL
import com.pwootage.metroidprime.formats.pak.PAKFile
import com.pwootage.metroidprime.utils.{DataTypeConversion, FileLocator, Logger, PrimeJacksonMapper}

class PakAssetIDDumper() {
  def dump(srcPath: String, destPath: String): Unit = {
    val dest = Paths.get(destPath)
    Files.createDirectories(dest)
    val paks = FileLocator.findFilesInBasePathWithExtension(srcPath, "Pak") ++
      FileLocator.findFilesInBasePathWithExtension(srcPath, "pak") ++
      FileLocator.findFilesInBasePathWithExtension(srcPath, "PAK")

    for (pak <- paks) {
      dumpPak(pak, dest)
    }
  }

  def dumpPak(pakPath: Path, destPath: Path): Unit = {
    val pak = new PAKFile(PrimeVersion.PRIME_1) //Doesn't matter, we're just looking for a compressed file
    val fin = Files.newInputStream(pakPath)
    val din = new DataInputStream(fin)
    pak.read(new PrimeDataFile(Some(din), None))
    fin.close()

    val str = pak.resources.map(_.id).mkString("\n")

    Files.write(destPath.resolve(pakPath.getFileName.toString + ".txt"),
      str.getBytes())
  }
}
