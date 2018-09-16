package io.holunda.zeebe.facebam.worker.thumbnailer.route

import com.fasterxml.jackson.databind.ObjectMapper
import io.holunda.zeebe.facebam.lib.worker.WorkerProperties
import io.holunda.zeebe.facebam.lib.worker.WorkerRouteBuilder
import io.holunda.zeebe.facebam.worker.thumbnailer.processor.CreateThumbnailProcessor
import io.zeebe.camel.api.ZippedString
import io.zeebe.camel.api.command.CompleteJobCommand
import org.apache.camel.Exchange
import org.apache.camel.LoggingLevel
import org.springframework.stereotype.Component


@Component
class CreateThumbnailRoute(objectMapper: ObjectMapper, properties: WorkerProperties) : WorkerRouteBuilder(objectMapper, properties) {

  override fun configure() {
    readJobsFromWorkerInbox("handle-job-${properties.jobType}")
      .copyEventDataToHeader()
      .log(LoggingLevel.INFO, "start working on file \${header.CamelFileName}")

      .loadWorkImage()

      .bean(CreateThumbnailProcessor::class.java)

      .toD(FILE_PARENT_NAME)
      .log(LoggingLevel.INFO, "wrote thumbnail $FILE_PARENT_NAME")

      .process(createCompleteCommand())
      .process(bodyToJson(CompleteJobCommand::class))
      .log(LoggingLevel.INFO, "send complete: \${body}")
      .to(brokerInbox())
  }


  override fun createCompleteCommand() = processor("createCompleteCommand") { exchange ->
    val imageName = exchange.`in`.getHeader(Exchange.FILE_NAME, String::class.java)
    val directory = exchange.`in`.getHeader(Exchange.FILE_PARENT, String::class.java)

    val cmd = CompleteJobCommand(
      jobEvent = exchange.`in`.getHeader(WorkerRouteBuilder.HEADER_JOB_EVENT, ZippedString::class.java),
      workerName = properties.name!!,
      jobType = properties.jobType!!,
      payload = objectMapper.writeValueAsString(
        Payload(Image(name = imageName, directory = directory))
      )
    )

    exchange.`in`.setBody(cmd, CompleteJobCommand::class.java)
  }
}


