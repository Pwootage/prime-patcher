package com.pwootage.metroidprime.utils

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.{JsonSerializer, ObjectMapper, SerializerProvider}
import com.fasterxml.jackson.module.scala.{DefaultScalaModule, JacksonModule}

object PrimeJacksonMapper {
  trait Hexable {
    def toHexString: String
  }

  val PrimeJacksonModule = new SimpleModule("Prime jackson Module")
    .addSerializer(classOf[Byte], new IntegerTypeHexSerializer[Byte](b => (b & 0xFFL).toHexString))
    .addSerializer(classOf[Short], new IntegerTypeHexSerializer[Short](s => (s & 0xFFFFL).toHexString))
    .addSerializer(classOf[Int], new IntegerTypeHexSerializer[Int](i => i.toHexString))
    .addSerializer(classOf[Long], new IntegerTypeHexSerializer[Long](l => l.toHexString))

  val mapper = new ObjectMapper()
    .registerModule(DefaultScalaModule)
    .registerModule(PrimeJacksonModule)

  val pretty = mapper.writerWithDefaultPrettyPrinter()

  class IntegerTypeHexSerializer[T](toHexString: T => String) extends JsonSerializer[T] {
    override def serialize(value: T, gen: JsonGenerator, serializers: SerializerProvider): Unit = {
      gen.writeString(s"0x${toHexString(value).toUpperCase}")
    }
  }
}
