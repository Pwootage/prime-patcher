package com.pwootage.metroidprime.randomizer

case class RandomizerConfig(invisibleItems: Boolean = false,
                            seed: Option[Int],
                            fixed: Map[String, RandomizerFixedLocation] = Map(),
                            patchFile: String = "./randomize.json",
                            logFile: Option[String]
                           )

case class RandomizerFixedLocation(id: Int, capacity: Int, amount: Int)