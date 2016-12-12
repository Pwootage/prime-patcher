package com.pwootage.metroidprime.randomizer

import com.pwootage.metroidprime.templates.Vector3FScriptPropertyValue
import com.pwootage.metroidprime.utils.DataTypeConversion

case class Prime1Item(name: String,
                      var count: Int,
                      item: Int,
                      capacity: Option[String],
                      amount: Option[String],
                      model: Int,
                      animSet: Int,
                      animCharacter: Option[String],
                      rotation: Option[Vector3FScriptPropertyValue],
                      xrayModel: Option[String],
                      xraySkin: Option[String]) {
  val capacityInt = capacity.map(DataTypeConversion.stringToLong(_).toInt)
  val amountInt = amount.map(DataTypeConversion.stringToLong(_).toInt)
  val animCharacterInt = animCharacter.map(DataTypeConversion.stringToLong(_).toInt)
  val xrayModelInt = xrayModel.map(DataTypeConversion.stringToLong(_).toInt)
  val xraySkinInt = xraySkin.map(DataTypeConversion.stringToLong(_).toInt)
}
