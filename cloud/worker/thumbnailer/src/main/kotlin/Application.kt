package io.holunda.zeebe.facebam.worker.watermarker

import mu.KLogging
import org.apache.camel.LoggingLevel
import org.apache.camel.builder.RouteBuilder
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

fun main(args: Array<String>) = runApplication<ThumbnailerApplication>(*args).let { Unit }

@SpringBootApplication
@EnableConfigurationProperties(ThumbnailerProperties::class)
class ThumbnailerApplication {
  companion object : KLogging()

  @Bean
  fun onStart(properties: ThumbnailerProperties) = CommandLineRunner {
    logger.info { ".... starting with properties: $properties" }
  }

  @Bean
  fun createThumbnailRoute(properties: ThumbnailerProperties) = object: RouteBuilder() {
    override fun configure() {
      from("file:\${properties.inbox}?include=.*\\.png")
          .id("create thumbnail")
          .log(LoggingLevel.INFO, "processing image")
          .to("file:\${properties.work}")
    }
  }
}

@ConfigurationProperties("facebam.thumbnailer")
data class ThumbnailerProperties(
    var inbox: String? = null,
    var work: String? = null
)
