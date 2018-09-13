package io.holunda.zeebe.facebam.worker.watermarker

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.holunda.zeebe.facebam.worker.common.WorkerProperties
import io.zeebe.camel.api.Json
import io.zeebe.camel.api.RegisterJobWorkerGateway
import io.zeebe.camel.api.ZippedString
import io.zeebe.camel.api.command.CompleteJobCommand
import io.zeebe.camel.api.command.RegisterJobWorkerCommand
import io.zeebe.camel.api.event.JobCreatedEvent
import io.zeebe.camel.api.zeebeRegisterJobWorkerGateway
import mu.KLogging
import org.apache.camel.CamelContext
import org.apache.camel.LoggingLevel
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.component.file.GenericFileMessage
import org.apache.camel.model.dataformat.JsonLibrary
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import java.io.File

fun main(args: Array<String>) = runApplication<WatermarkerApplication>(*args).let { Unit }

@SpringBootApplication
@EnableConfigurationProperties(WorkerProperties::class)
class WatermarkerApplication(
  private val properties: WorkerProperties,
  private val camel: CamelContext
) {

  companion object : KLogging()


  @Bean
  fun registerJobWorkerGateway() = camel.zeebeRegisterJobWorkerGateway()

  @Bean
  fun registerWorker() = object : RouteBuilder() {
    override fun configure() {
      from(RegisterJobWorkerGateway.ENDPOINT)
        .id("publish-registration")
        .log(LoggingLevel.INFO, "publish registration for watermarker")
        .marshal().json(JsonLibrary.Jackson, RegisterJobWorkerCommand::class.java, true)
        .to("file:${properties.broker?.inbox}?fileName=watermarker-watermark.register")
    }
  }

  @Bean
  fun runJob() = object : RouteBuilder() {
    override fun configure() {
      from("""file:${properties.worker?.inbox}?include=.*\.job""")
        // fixme move extension to api
        .process {
          val msg = it.getIn(GenericFileMessage::class.java)
          val content = File(msg.genericFile.absoluteFilePath).readText()
          val evt = jacksonObjectMapper().readValue(content, JobCreatedEvent::class.java)

          it.`in`.body = evt
          it.`in`.setHeader("jobEvent", evt.jobEvent)
        }
        .process {
          val job = it.`in`.getMandatoryBody(JobCreatedEvent::class.java)

          val cmd = CompleteJobCommand(
            job.jobType,
            job.workerName,
            it.`in`.getHeader("jobEvent", ZippedString::class.java),
            """{"foo":"bar"}"""
          )
          it.`in`.body =  cmd
        }
        // to json
        .process {
          val cmd = it.`in`.getMandatoryBody(CompleteJobCommand::class.java)
          val json = jacksonObjectMapper().writeValueAsString(cmd)
          it.`in`.body = json }
        .to("file:${properties.broker?.inbox}?fileName=watermarker-watermark.complete") // fixme: jobId
    }
  }


  @Bean
  fun run(gateway: RegisterJobWorkerGateway) = CommandLineRunner {
    logger.info { ".... starting with properties: $properties" }
    logger.info { ".... camel: ${camel.componentNames}" }

    gateway.send(
      RegisterJobWorkerCommand(
        jobType = "watermark",
        workerName = properties.name!!,
        to = "file:${properties.worker?.inbox}?fileName=watermark.job",
        toJson = true
      )
    )
  }


}
