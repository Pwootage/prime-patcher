package com.pwootage.metroidprime.randomizer

case class RandomizerConfig(seed: Option[Long],
                            fixed: Seq[RandomizerFixedLocation] = Seq(),
                            outDir: String
                           )

case class RandomizerFixedLocation(world: String, name: String, item: String)