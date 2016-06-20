package com.pwootage.metroidprime.utils

import java.io.{ByteArrayInputStream, IOException, InputStream}

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode

import scala.collection.JavaConversions._

object PrimeDiffUtils {
  def diffByteArrayDebug(a: Array[Byte], b: Array[Byte]): Unit = {
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

  def firstDifference(a: Array[Byte], b: Array[Byte]): Int = {
    firstDifference(new ByteArrayInputStream(a), new ByteArrayInputStream(b), a.length)
  }

  def firstDifference(a: InputStream, b: InputStream, length: Int): Int = {
    var i = 0
    val buff1 = new Array[Byte](1024)
    val buff2 = new Array[Byte](1024)
    while (i < length) {
      val toRead = Math.min(buff1.length, length - i)
      if (a.read(buff1, 0, toRead) != toRead) return i
      if (b.read(buff2, 0, toRead) != toRead) return i
      for (j <- 0 until toRead) {
        if (buff1(j) != buff2(j)) {
          return i
        }
        i += 1
      }
    }
    -1
  }

  def generateDiff[T](in1: Array[T], in2: Array[T]) = {
    var a = in1.zipWithIndex.toList
    var b = in2.zipWithIndex.toList

    var diff = Seq[(String, Int, T)]()

    var i1 = 0
    var i2 = 0
    while (a.nonEmpty && b.nonEmpty) {
      val ah :: at = a
      val bh :: bt = b

      if (ah._1 == bh._1) {
        //No difference
        a = at
        b = bt
      } else {
        val distInB = bt.indexWhere(_._1 == ah._1)
        val distInA = at.indexWhere(_._1 == bh._1)

        if (distInB < 0) {
          //Not in B, so deleted
          diff +:=("REMOVE", ah._2, ah._1)
          a = at
        } else if (distInA < 0) {
          //Not in A, so added
          diff +:=("ADD", bh._2, bh._1)
          b = bt
        } else if (distInB < distInA) {
          //(probably) not in A, so added
          diff +:=("ADD", bh._2, bh._1)
          b = bt
        } else {
          //Not in B, so deleted
          diff +:=("REMOVE", ah._2, ah._1)
          a = at
        }
      }
    }

    while (a.nonEmpty) {
      //Not in B, so deleted
      val ah :: at = a
      diff +:=("REMOVE", ah._2, ah._1)
      a = at
    }

    while (b.nonEmpty) {
      //Not in B, so deleted
      val bh :: bt = b
      diff +:=("ADD", bh._2, bh._1)
      b = bt
    }

    diff.reverse
  }

  def recursiveJsonDiff(a: ObjectNode, b: ObjectNode): ObjectNode = {
    val res = PrimeJacksonMapper.mapper.createObjectNode()
    for (child <- b.fieldNames()) {
      val childa = a.get(child)
      val childb = b.get(child)
      if (childa != childb) {
        if (b.get(child).isObject) {
          res.set(child, recursiveJsonDiff(a.get(child).asInstanceOf[ObjectNode], b.get(child).asInstanceOf[ObjectNode]))
        } else {
          res.set(child, childb)
        }
      }
    }
    res
  }
}
