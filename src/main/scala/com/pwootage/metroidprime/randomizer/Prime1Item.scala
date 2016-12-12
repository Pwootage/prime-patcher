package com.pwootage.metroidprime.randomizer

import com.pwootage.metroidprime.templates.Vector3FScriptPropertyValue

case class Prime1Item(name: String,
                      var count: Int,
                      item: Int,
                      capacity: Option[Int],
                      amount: Option[Int],
                      model: Int,
                      animSet: Int,
                      animCharacter: Option[Int],
                      rotation: Option[Vector3FScriptPropertyValue])
