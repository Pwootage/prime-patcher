package com.pwootage.metroidprime.templates

import scala.xml.{Node, NodeSeq}

object XMLUtils {
  implicit class RichNode(val node: NodeSeq) extends AnyVal {
    def optionalText: Option[String] = {
      node.headOption.map(_.text)
    }

    def as[T <: XmlSerializable](create: () => T): T = {
      val res = create()
      res.fromXml(node)
      res
    }

    def as[T <: XmlSerializable](create: (NodeSeq) => T): T = {
      val res = create(node)
      if (res == null) {
        println(s"Not handling $node")
      } else {
        res.fromXml(node)
      }
      res
    }

    def array[T <: XmlSerializable](create: () => T): Seq[T] = {
      node.map(n => {
        val res = create()
        res.fromXml(n)
        res
      })
    }

    def array[T <: XmlSerializable](create: NodeSeq => T): Seq[T] = {
      node.map(n => {
        val res = create(n)
        if (res == null) {
          println(s"Not handling $n")
        } else {
          res.fromXml(n)
        }
        res
      })
    }
  }
}
