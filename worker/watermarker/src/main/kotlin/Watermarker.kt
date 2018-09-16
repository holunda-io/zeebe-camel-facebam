package io.holunda.zeebe.facebam.worker.watermarker

import io.holunda.zeebe.facebam.lib.worker.JobWorkerConfiguration
import io.holunda.zeebe.facebam.lib.worker.WorkerProperties
import io.holunda.zeebe.facebam.lib.worker.sendRegistration
import io.zeebe.camel.api.RegisterJobWorkerGateway
import mu.KLogging
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import

fun main(args: Array<String>) = runApplication<WatermarkerApplication>(*args).let { Unit }

@SpringBootApplication
@EnableConfigurationProperties(WorkerProperties::class)
@Import(JobWorkerConfiguration::class)
class WatermarkerApplication(private val properties: WorkerProperties) {

  companion object : KLogging()

  @Bean
  fun run(gateway: RegisterJobWorkerGateway) = CommandLineRunner {
    logger.info { ".... starting with properties: $properties" }

    gateway.sendRegistration(properties)
  }
}
