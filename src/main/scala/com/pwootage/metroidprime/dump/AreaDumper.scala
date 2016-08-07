package com.pwootage.metroidprime.dump

import java.io.DataInputStream
import java.nio.file.{Files, Path, Paths}

import com.pwootage.metroidprime.formats.common.{Face, PrimeFileType}
import com.pwootage.metroidprime.formats.io.PrimeDataFile
import com.pwootage.metroidprime.formats.mlvl.{Area, MLVL}
import com.pwootage.metroidprime.formats.mrea.MREA
import com.pwootage.metroidprime.formats.mrea.collision.Collision
import com.pwootage.metroidprime.utils.{DataTypeConversion, FileLocator, Logger, PrimeJacksonMapper}

class AreaDumper() {
  def dump(srcPath: String, destPath: String): Unit = {
    val dest = Paths.get(destPath)
    Files.createDirectories(dest)
    val mlvls = FileLocator.findFilesInBasePathWithExtension(srcPath, "MLVL")

    for (mlvl <- mlvls) {
      dumpMlvl(mlvl, dest)
    }
  }

  def dumpMlvl(mlvlPath: Path, destPath: Path): Unit = {
    val mlvl = new MLVL
    val fin = Files.newInputStream(mlvlPath)
    val din = new DataInputStream(fin)
    mlvl.read(new PrimeDataFile(Some(din), None))
    fin.close()

    Logger.info(s"Loading world ${DataTypeConversion.intToPaddedHexString(mlvl.header.worldNameSTRG)}")
    Logger.info(s"World contains ${mlvl.areas.length} areas")

    Files.write(destPath.resolve(mlvlPath.getFileName.toString + ".json"),
      PrimeJacksonMapper.pretty.writeValueAsBytes(mlvl.areas))
  }
}
