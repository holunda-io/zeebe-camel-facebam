package io.holunda.zeebe.facebam.client

import mu.KLogging
import org.apache.camel.builder.RouteBuilder
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
@EnableConfigurationProperties(ClientConfigurationProperties::class)
class ClientApplication {

  companion object : KLogging()

  /**
   * Reads from client directory and copies to cloud inbox
   */
  @Bean
  fun uploadImage(properties: ClientConfigurationProperties) = object : RouteBuilder() {
    override fun configure() {
      from("file:${properties.filesystem.source}")
          .routeId("upload-image-to-cloud")
          .to("file:${properties.cloudRoot}/foo")
    }
  }

  @Bean
  fun onStart(properties: ClientConfigurationProperties) = CommandLineRunner {
    logger.info { ".... starting with properties: $properties" }
  }

}

fun main(args: Array<String>) = runApplication<ClientApplication>().let { Unit }


@ConfigurationProperties("facebam.client")
data class ClientConfigurationProperties(
    val filesystem: Filesystem = Filesystem(),
    var cloudRoot: String? = null
) {
  data class Filesystem(
      var source: String? = null,
      var destination: String? = null
  )
}
