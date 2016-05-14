package com.pwootage.metroidprime.utils

object ByteDiffer {
  def diff(a: Array[Byte], b: Array[Byte]): Unit = {
    if (a.length != b.length) {
      println(s"Lengths differ ${a.length} ${b.length}")
    }
    for (i <- a.indices) {
      if (i < a.length && i < b.length && a(i) != b(i)) {
        println(s"Bytes differ at ${i.toHexString} ${a(i)} ${b(i)}")
      }
      if (i >= a.length) {
        println(s"Extra b byte ${b(i)}")
      }
      if (i >= b.length) {
        println(s"Extra a byte ${a(i)}")
      }
    }
    println("end of diff")
  }
}
