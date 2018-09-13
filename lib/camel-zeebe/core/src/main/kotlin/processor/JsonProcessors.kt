package io.zeebe.camel.processor

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.camel.Exchange
import org.apache.camel.Processor

fun <T : Any> ObjectMapper.bodyFromJson(bodyType: Class<T>): Processor = object : Processor {
  override fun process(exchange: Exchange) {
    val json = exchange.`in`.getMandatoryBody(String::class.java)

    exchange.`in`.body = readValue(json, bodyType)
  }

  override fun toString() = "fromJson(${bodyType.canonicalName})"
}

fun <T : Any> ObjectMapper.bodyToJson(bodyType: Class<T>): Processor = object : Processor {
  override fun process(exchange: Exchange) {
    val body = exchange.`in`.getMandatoryBody(bodyType)

    exchange.`in`.body = writeValueAsString(body)
  }

  override fun toString() = "toJson(${bodyType.canonicalName})"
}
