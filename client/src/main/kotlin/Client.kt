package io.holunda.zeebe.facebam.client

import mu.KLogging
import org.apache.camel.LoggingLevel
import org.apache.camel.builder.RouteBuilder
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

fun main(args: Array<String>) = runApplication<ClientApplication>(*args).let { Unit }

@SpringBootApplication
@EnableConfigurationProperties(ClientProperties::class)
class ClientApplication {

  companion object : KLogging()

  /**
   * Reads from client directory and copies to cloud inbox
   */
  @Bean
  fun uploadImage(properties: ClientProperties) = object : RouteBuilder() {
    override fun configure() {
      from("""file:${properties.client.outbox}?include=.*\.png$""")
          .id("upload-image-to-cloud")
          .log(LoggingLevel.INFO, "uploading image")
          .to("""file:${properties.cloud.inbox}""")
    }
  }

  @Bean
  fun onStart(properties: ClientProperties) = CommandLineRunner {
    logger.info { ".... starting with properties: $properties" }
  }
}

@ConfigurationProperties("facebam")
data class ClientProperties(
    val client: FS = FS(),
    val cloud: FS = FS()
) {
  data class FS(
      var inbox: String? = null,
      var outbox: String? = null
  )
}
