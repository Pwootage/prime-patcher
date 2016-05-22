package com.pwootage.metroidprime.dump

import java.io.{DataInputStream, FileWriter}
import java.nio.file.{Files, Path, Paths}

import com.pwootage.metroidprime.formats.common.{Face, PrimeFileType}
import com.pwootage.metroidprime.formats.io.PrimeDataFile
import com.pwootage.metroidprime.formats.mlvl.{Area, MLVL}
import com.pwootage.metroidprime.formats.mrea.MREA
import com.pwootage.metroidprime.formats.mrea.collision.Collision
import com.pwootage.metroidprime.utils.{DataTypeConversion, FileLocator, Logger, PrimeJacksonMapper}

class CollisionDumper() {
  def dump(srcPath: String, destPath: String): Unit = {
    val dest = Paths.get(destPath)
    val mlvls = FileLocator.findFilesInBasePathWithExtension(srcPath, "MLVL")

    for (mlvl <- mlvls) {
      dumpMlvl(mlvl, dest.resolve(mlvl.getFileName.toString))
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

    for (area <- mlvl.areas) {
      dumpArea(mlvlPath, area, destPath)
    }

    val areas = Map(mlvl.areas.zipWithIndex.map(tuple => {
      val (area, index) = tuple
      DataTypeConversion.intToPaddedHexString(area.header.MREA) -> {
        Map(
          "boundingBox" -> area.header.boundingBox,
          "transform" -> area.header.transform,
          "index" -> index
        )
      }
    }):_*)
    Files.write(destPath.resolve("areas.json"), PrimeJacksonMapper.pretty.writeValueAsBytes(areas))
  }

  def dumpArea(mlvlPath: Path, area: Area, destPath: Path): Unit = {
    Files.createDirectories(destPath)

    val mreaName: String = DataTypeConversion.intPrimeResourceNameToStr(area.header.MREA, PrimeFileType.MREA.fourCC)
    val mreaPath = mlvlPath.getParent.resolve(mreaName)


    val fin = Files.newInputStream(mreaPath)
    val din = new DataInputStream(fin)
    val mrea = new MREA
    mrea.read(new PrimeDataFile(Some(din), None))
    fin.close()

    val collision = mrea.parseCollision

    Logger.progressResetLine(s"Dumping collision for ${area.areaName}/${DataTypeConversion.intToPaddedHexString(area.header.id)}")
    writeCollisionToFile(collision, destPath.resolve(mreaName + ".obj"))
  }

  def writeCollisionToFile(coll: Collision, path: Path): Unit = {
    val (verts, faces) = collisionToVertsAndFaces(coll)

    Files.deleteIfExists(path)
    val objOut = Files.newBufferedWriter(path)
    objOut.write(s"# Vertexes (${coll.verts.length})\n")
    for (v <- verts) objOut.write(s"v ${v.x} ${v.y} ${v.z}\n")
    objOut.write(s"# Faces (${coll.faces.length})\n")
    for (f <- faces) objOut.write(s"f ${f.ind1 + 1} ${f.ind2 + 1} ${f.ind3 + 1}\n")
    objOut.close()
  }

  def collisionToVertsAndFaces(coll: Collision) = {
    val verts = coll.verts
    val faces = for ((f, i) <- coll.faces.zipWithIndex) yield {
      val flags = coll.collisionMaterialFlags(coll.facePropertyIndices(i))
      val line1 = coll.lines(f.ind1)
      val line2 = coll.lines(f.ind2)
      val line3 = coll.lines(f.ind3)

      val (l1a, l1b) = (coll.verts(line1.ind1), coll.verts(line1.ind2))
      val (l2a, l2b) = (coll.verts(line2.ind1), coll.verts(line2.ind2))
      val (l3a, l3b) = (coll.verts(line3.ind1), coll.verts(line3.ind2))

      val i1 = line1.ind1
      val (i2, otherLine) = {
        if (line1.ind1 == line2.ind1) {
          (line2.ind2, line3)
        } else if (line1.ind1 == line2.ind2) {
          (line2.ind1, line3)
        } else if (line1.ind1 == line3.ind1) {
          (line3.ind2, line2)
        } else {
          (line3.ind1, line2)
        }
      }
      val i3 = {
        if (i2 == otherLine.ind1) {
          otherLine.ind2
        } else {
          otherLine.ind1
        }
      }
      val face = new Face
      if ((flags & 0x2000000) > 0) {
        face.ind1 = i1
        face.ind2 = i2
        face.ind3 = i3
      } else {
        face.ind1 = i3
        face.ind2 = i2
        face.ind3 = i1
      }
      face
    }

    (verts, faces)
  }
}
