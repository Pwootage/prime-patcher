package com.pwootage.metroidprime.formats.scly

import com.pwootage.metroidprime.formats.BinarySerializable
import com.pwootage.metroidprime.formats.io.PrimeDataFile

class ScriptObjectConnection extends BinarySerializable {
  var state: Int = -1
  var message: Int = -1
  var targetObject: Int = -1

  override def write(f: PrimeDataFile): Unit = {
    f.write32(state)
    f.write32(message)
    f.write32(targetObject)
  }

  override def read(f: PrimeDataFile): Unit = {
    state = f.read32()
    message = f.read32()
    targetObject = f.read32()
  }

  def stateEnum = Prime1ScriptObjectState.fromID(state)
  def stateString = stateEnum.toString

  def messageEnum = Prime1ScriptObjectMessage.fromID(message)
  def messageString = messageEnum.toString

  override def toString = s"ScriptObjectConnection($targetObject, $stateString, $messageString)"
}
