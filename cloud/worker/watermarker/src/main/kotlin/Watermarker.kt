package io.holunda.zeebe.facebam.worker.watermarker

import io.holunda.zeebe.facebam.worker.common.WorkerProperties
import io.zeebe.camel.api.RegisterJobWorkerGateway
import io.zeebe.camel.api.command.RegisterJobWorkerCommand
import io.zeebe.camel.api.zeebeRegisterJobWorkerGateway
import mu.KLogging
import org.apache.camel.CamelContext
import org.apache.camel.LoggingLevel
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.model.dataformat.JsonLibrary
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
){

  companion object : KLogging()

  @Bean
  fun addWatermarkRoute() = object : RouteBuilder() {

    override fun configure() {
     from("file:${properties.worker?.inbox}" +
        "?include=.*\\.png"
      ).id("add watermark to image")

        .log(LoggingLevel.INFO, "processing image")

        .to("file:${properties.worker?.work}")
    }
  }

  @Bean
  fun registerJobWorkerGateway() = camel.zeebeRegisterJobWorkerGateway()

  @Bean
  fun registerWorker() = object: RouteBuilder() {
    override fun configure() {
      from(RegisterJobWorkerGateway.ENDPOINT)
        .id("publish-registration")
        .log(LoggingLevel.INFO, "publish registration for watermarker")
        .marshal().json(JsonLibrary.Jackson, RegisterJobWorkerCommand::class.java, true)
        .to("file:${properties.broker?.inbox}?fileName=watermarker-watermark")
    }

  }


  @Bean
  fun run(gateway: RegisterJobWorkerGateway) = CommandLineRunner {
    logger.info { ".... starting with properties: $properties" }
    logger.info { ".... camel: ${camel.componentNames}" }

    gateway.send(RegisterJobWorkerCommand(jobType = "watermark", workerName = properties.name!!, to = "file:${properties.worker?.inbox}"))
  }


}
