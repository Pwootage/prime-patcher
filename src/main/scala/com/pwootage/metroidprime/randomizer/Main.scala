package com.pwootage.metroidprime.randomizer

import java.io.RandomAccessFile
import java.nio.file.Paths

import com.pwootage.metroidprime.formats.io.PrimeDataFile
import com.pwootage.metroidprime.formats.mlvl.MLVL

object Main {
  def main(args: Array[String]) {
    val file = "K:/roms/gc/mp-extracted/mp1/Metroid2-pak/83f6ff6f.MLVL"
//    val file = "K:/roms/gc/mp-extracted/mp2/Metroid1-pak/3bfa3eff.MLVL"
    val raf = new RandomAccessFile(file, "rw")
    val pf = new PrimeDataFile(raf)

    val mlvl = new MLVL
    mlvl.read(pf)

    pf.close()

    println(mlvl)
  }
}
