package com.pwootage.metroidprime.randomizer

case class RandomizerConfig(seed: Option[Int],
                            fixed: Map[String, RandomizerFixedLocation] = Map(),
                            outDir: String
                           )

case class RandomizerFixedLocation(id: Int, capacity: Int, amount: Int)