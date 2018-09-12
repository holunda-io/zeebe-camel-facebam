package io.holunda.zeebe.facebam.worker.watermarker

import io.holunda.zeebe.facebam.worker.common.WorkerProperties
import mu.KLogging
import org.apache.camel.CamelContext
import org.apache.camel.LoggingLevel
import org.apache.camel.builder.RouteBuilder
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

fun main(args: Array<String>) = runApplication<WatermarkerApplication>(*args).let { Unit }

@SpringBootApplication
@EnableConfigurationProperties(WorkerProperties::class)
class WatermarkerApplication(
  private val properties: WorkerProperties,
  private val camel: CamelContext
) : CommandLineRunner {

  companion object : KLogging()

  @Bean
  fun addWatermarkRoute() = object : RouteBuilder() {

    override fun configure() {
      from("file:${properties.inbox}" +
        "?include=.*\\.png"
      ).id("add watermark to image")

        .log(LoggingLevel.INFO, "processing image")

        .to("file:${properties.work}")
    }
  }


  override fun run(vararg args: String?) {
    logger.info { ".... starting with properties: $properties" }
    logger.info { ".... camel: ${camel.componentNames}" }

  }



}
