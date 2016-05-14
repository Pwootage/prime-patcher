package com.pwootage.metroidprime.formats.scly.prime1ScriptObjects

import com.pwootage.metroidprime.formats.common.Vec3
import com.pwootage.metroidprime.formats.io.PrimeDataFile
import com.pwootage.metroidprime.formats.scly.prime1ScriptObjects.common.{ActorParams, AnimationParams}

class Pickup extends ScriptObjectInstanceBase {
  var pos = new Vec3
  var rotation = new Vec3
  var scale = new Vec3
  var collisionScale = new Vec3
  var scanOffset = new Vec3
  var item: Int = -1
  var capacity: Int = -1
  var amount: Int = -1
  var dropRate: Float = Float.NaN
  var lifeTime: Float = Float.NaN
  var fadeLength: Float = Float.NaN
  var modelCDML: Int = -1
  val animParams = new AnimationParams
  val actorParams = new ActorParams
  var active: Boolean = false
  var spawnDelay: Float = Float.NaN
  var particlePART: Int = -1


  override def write(f: PrimeDataFile): Unit = {
    super.write(f)
    f.write(pos)
    f.write(rotation)
    f.write(scale)
    f.write(collisionScale)
    f.write(scanOffset)
    f.write32(item)
    f.write32(capacity)
    f.write32(amount)
    f.writeFloat(dropRate)
    f.writeFloat(lifeTime)
    f.writeFloat(fadeLength)
    f.write32(modelCDML)
    f.write(animParams)
    f.write(actorParams)
    f.writeBool(active)
    f.writeFloat(spawnDelay)
    f.write32(particlePART)
  }

  override def read(f: PrimeDataFile): Unit = {
    super.read(f)
    f.read(pos)
    f.read(rotation)
    f.read(scale)
    f.read(collisionScale)
    f.read(scanOffset)
    item = f.read32()
    capacity = f.read32()
    amount = f.read32()
    dropRate = f.readFloat()
    lifeTime = f.readFloat()
    fadeLength = f.readFloat()
    modelCDML = f.read32()
    f.read(animParams)
    f.read(actorParams)
    active = f.readBool()
    spawnDelay = f.readFloat()
    particlePART = f.read32()
  }

  def itemEnum = Prime1PickupType.fromID(item)
  def itemName = itemEnum.toString
}
