package com.pwootage.metroidprime.templates

class Vector3FScriptPropertyValue {
  var x: Float = 0
  var y: Float = 0
  var z: Float = 0

  def this(str: String) {
    this()
    val split = str.split(",").map(_.trim)
    x = split(0).toFloat
    y = split(1).toFloat
    z = split(2).toFloat
  }


  override def toString = s"Vector3FScriptPropertyValue($x, $y, $z)"
}
