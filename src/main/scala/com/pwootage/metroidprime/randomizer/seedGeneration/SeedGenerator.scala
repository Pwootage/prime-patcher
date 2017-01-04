package com.pwootage.metroidprime.randomizer.seedGeneration

import java.io.IOException
import java.util.Random

import com.pwootage.metroidprime.randomizer.{Prime1Item, Prime1ItemLocation, RandomizerConfig}
import com.pwootage.metroidprime.utils.{PrimeJacksonMapper, ResourceUtils}

case class ItemLocation(world: String, name: String)

case class Item(name: String)

class SeedGenerator(config: RandomizerConfig) {
  private var itemPool = {
    val src = ResourceUtils.resourceAsString("/randomizer/items/prime1Items.json")
    PrimeJacksonMapper.mapper.readValue(src, classOf[Array[Prime1Item]])
  }.flatMap(item => {
    Array.fill(item.count)(new Item(item.name))
  }).toSeq

  private var itemLocations = {
    val src = ResourceUtils.resourceAsString("/randomizer/items/prime1ItemLocations.json")
    PrimeJacksonMapper.mapper.readValue(src, classOf[Array[Prime1ItemLocation]])
  }.map(v => ItemLocation(v.world, v.description)).toSet

  private var items = Map[ItemLocation, Item]()

  def itemMap = items

  generate()

  private def generate(): Unit = {
    setFixedItems()
    val rng = new Random(config.seed.getOrElse(System.currentTimeMillis()))

    while (itemLocations.nonEmpty) {
      val loc = itemLocations.head
      val item = itemPool(rng.nextInt(itemPool.size))
      setItem(loc, item)
    }
  }

  private def setFixedItems(): Unit = {
    for (fixed <- config.fixed) {
      val location = ItemLocation(fixed.world, fixed.name)
      val item = Item(fixed.item)
      setItem(location, item)
    }
  }

  private def setItem(location: ItemLocation, item: Item): Unit = {
    items += location -> item
    itemLocations -= location
    val ind = itemPool.indexOf(item)
    if (ind < 0) {
      throw new IOException(s"Can't find $item in pool")
    } else {
      itemPool = itemPool.slice(0, ind) ++ itemPool.slice(ind + 1, itemPool.size)
    }
  }

}
