package com.pwootage.metroidprime.templates

class ColorScriptPropertyValue {
  var r: Float = 0
  var g: Float = 0
  var b: Float = 0
  var a: Float = 1

  def this(str: String) {
    this()
    val split = str.split(",").map(_.trim)
    r = split(0).toFloat
    g = split(1).toFloat
    b = split(2).toFloat
    a = split(3).toFloat
  }
}
