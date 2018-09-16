package io.holunda.zeebe.facebam.worker.thumbnailer

import io.holunda.zeebe.facebam.lib.worker.JobWorkerConfiguration
import io.holunda.zeebe.facebam.lib.worker.WorkerProperties
import io.holunda.zeebe.facebam.lib.worker.sendRegistration
import io.zeebe.camel.api.RegisterJobWorkerGateway
import mu.KLogging
import org.apache.camel.CamelContext
import org.apache.camel.Exchange
import org.apache.camel.Processor
import org.apache.camel.RoutesBuilder
import org.apache.camel.builder.RouteBuilder
import org.imgscalr.Scalr
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.stereotype.Component
import java.io.ByteArrayOutputStream
import java.io.InputStream
import javax.imageio.ImageIO
import java.awt.image.BufferedImage

fun main(args: Array<String>) = runApplication<ThumbnailerApplication>(*args).let { Unit }

@SpringBootApplication
@EnableConfigurationProperties(WorkerProperties::class)
@Import(JobWorkerConfiguration::class)
class ThumbnailerApplication(private val properties: WorkerProperties) {
  companion object : KLogging()

  @Bean
  fun onStart(gateway: RegisterJobWorkerGateway) = CommandLineRunner {
    logger.info { ".... starting with properties: $properties" }
    gateway.sendRegistration(properties)
  }
}
