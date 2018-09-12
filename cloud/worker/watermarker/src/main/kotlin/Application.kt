package io.holunda.zeebe.facebam.worker.watermarker

import io.holunda.zeebe.facebam.worker.common.WorkerProperties
import mu.KLogging
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
class WatermarkerApplication {
  companion object : KLogging()

  @Bean
  fun onStart(properties: WorkerProperties) = CommandLineRunner {
    logger.info { ".... starting with properties: $properties" }
  }

  @Bean
  fun addWatermarkRoute(properties: WorkerProperties) = object : RouteBuilder() {
    override fun configure() {
      from("file:${properties.inbox}?include=.*\\.png")
        .id("add watermark to image")
        .log(LoggingLevel.INFO, "processing image")
        .to("file:${properties.work}")
    }
  }
}
