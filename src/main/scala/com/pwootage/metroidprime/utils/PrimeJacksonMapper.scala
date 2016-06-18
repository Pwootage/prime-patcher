package com.pwootage.metroidprime.utils

import com.fasterxml.jackson.core.{JsonGenerator, JsonParser}
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind._
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
    .addDeserializer(classOf[Byte], new IntegerTypeHexDeserializer[Byte](_.toByte))
    .addDeserializer(classOf[Short], new IntegerTypeHexDeserializer[Short](_.toShort))
    .addDeserializer(classOf[Int], new IntegerTypeHexDeserializer[Int](_.toInt))
    .addDeserializer(classOf[Long], new IntegerTypeHexDeserializer[Long](x => x))

  val mapper = new ObjectMapper()
    .registerModule(DefaultScalaModule)
    .registerModule(PrimeJacksonModule)

  val pretty = mapper.writerWithDefaultPrettyPrinter()

  class IntegerTypeHexSerializer[T](toHexString: T => String) extends JsonSerializer[T] {
    override def serialize(value: T, gen: JsonGenerator, serializers: SerializerProvider): Unit = {
      gen.writeString(s"0x${toHexString(value).toUpperCase}")
    }
  }

  class IntegerTypeHexDeserializer[T](convert: Long => T) extends JsonDeserializer[T] {
    override def deserialize(p: JsonParser, ctxt: DeserializationContext): T = {
      val long = if (p.getCurrentToken.isNumeric) {
        p.getLongValue
      } else {
        val str = p.getValueAsString
        if (str.startsWith("0x")) {
          java.lang.Long.parseLong(str.substring(2), 16)
        } else if (str.startsWith("0b")) {
          java.lang.Long.parseLong(str.substring(2), 2)
        } else {
          java.lang.Long.parseLong(str)
        }
      }
      convert(long)
    }
  }
}
