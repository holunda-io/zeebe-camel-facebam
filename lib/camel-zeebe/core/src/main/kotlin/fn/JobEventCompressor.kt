@file:Suppress(PACKAGE)
package io.zeebe.camel.core.fn

import io.zeebe.camel.api.PACKAGE
import io.zeebe.client.api.events.JobEvent
import io.zeebe.client.api.record.ZeebeObjectMapper
import io.zeebe.client.impl.data.ZeebeObjectMapperImpl
import java.io.ByteArrayOutputStream
import java.util.*
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream
import kotlin.text.Charsets.UTF_8

class JobEventCompressor(private val objectMapper: ZeebeObjectMapper = ZeebeObjectMapperImpl()) {

  fun encode(msg : JobEvent) : String {
    val json = objectMapper.toJson(msg)
    val bos = ByteArrayOutputStream()
    GZIPOutputStream(bos).bufferedWriter(UTF_8).use { it.write(json) }
    val bytes = bos.toByteArray()
    return Base64.getEncoder().encodeToString(bytes)
  }


  fun decode(msg : String) :JobEvent {
    val bytes = Base64.getDecoder().decode(msg)
    val json = GZIPInputStream(bytes.inputStream()).bufferedReader(UTF_8).use { it.readText() }

    return objectMapper.fromJson(json, JobEvent::class.java)
  }

}
