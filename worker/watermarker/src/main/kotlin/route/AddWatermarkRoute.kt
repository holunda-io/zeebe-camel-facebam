package io.holunda.zeebe.facebam.worker.watermarker.route

import com.fasterxml.jackson.databind.ObjectMapper
import io.holunda.zeebe.facebam.lib.worker.WorkerProperties
import io.holunda.zeebe.facebam.lib.worker.WorkerRouteBuilder
import io.holunda.zeebe.facebam.worker.watermarker.processor.WatermarkerProcessor
import io.zeebe.camel.api.ZippedString
import io.zeebe.camel.api.command.CompleteJobCommand
import org.apache.camel.Exchange
import org.springframework.stereotype.Component

@Component
class AddWatermarkRoute(objectMapper: ObjectMapper, properties: WorkerProperties) : WorkerRouteBuilder(objectMapper, properties) {

  override fun configure() {
    readJobsFromWorkerInbox("handle-job-watermark")

      .copyEventDataToHeader()

      .loadWorkImage()

      .bean(WatermarkerProcessor::class.java)
      .toD("file:\${header.CamelFileParent}")

      .process(createCompleteCommand())
      .process(bodyToJson(CompleteJobCommand::class))
      .to(brokerInbox())
  }

  override fun createCompleteCommand() = processor("create-complete-commands") { exchange ->
    val imageName = exchange.`in`.getHeader(Exchange.FILE_NAME, String::class.java)
    val directory = exchange.`in`.getHeader(Exchange.FILE_PARENT, String::class.java)

    val cmd = CompleteJobCommand(
      jobEvent = exchange.`in`.getHeader(HEADER_JOB_EVENT, ZippedString::class.java),
      workerName = properties.name!!,
      jobType = properties.jobType!!,
      payload = objectMapper.writeValueAsString(Payload(
        Image(name = imageName, directory = directory))
      )
    )

    exchange.`in`.setBody(cmd, CompleteJobCommand::class.java)
  }

}



