package com.pwootage.metroidprime.formats.pak

import com.pwootage.metroidprime.formats.common.PrimeVersion

case class BasicResourceList(primeVersion: PrimeVersion, namedResources: Map[String,String], resources: Seq[String])
