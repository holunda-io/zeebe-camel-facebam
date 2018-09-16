package io.zeebe.camel.lib.json

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.component.file.GenericFileMessage
import org.apache.camel.model.RouteDefinition
import java.io.File
import kotlin.reflect.KClass

val objectMapper = jacksonObjectMapper()
  .enable(SerializationFeature.INDENT_OUTPUT)!!

fun <T : Any> RouteDefinition.fileContentToObject(type: KClass<T>) = this.process {
  val file = it.getMessage(GenericFileMessage::class.java)
  val content = File(file.genericFile.absoluteFilePath).readText()
  val bean = objectMapper.readValue(content, type.java)
  it.`in`.setBody(bean, type.java)
}

fun <T : Any> RouteBuilder.fromJsonFile(
  id: String = "fromJsonFile",
  type: KClass<T>,
  directory: String,
  suffix: String,
  initialDelay: Int = 1000,
  noop: Boolean = false
  ) = this
  .from("""file:$directory?include=.*\.$suffix$&initialDelay=$initialDelay&noop=$noop""".trimMargin())
  .id(id)
  .fileContentToObject(type) // fixme: jobId

fun <T : Any> RouteDefinition.toJsonFile(type: KClass<T>, directory: String, fileName: String) = this
  .process {
    val cmd = it.`in`.getMandatoryBody(type.java)
    val json = objectMapper.writeValueAsString(cmd)
    it.`in`.body = json
  }
  .to("file:${directory}?fileName=${fileName}")!!
