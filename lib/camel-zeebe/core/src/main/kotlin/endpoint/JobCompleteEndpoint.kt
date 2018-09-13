package io.zeebe.camel.endpoint

import io.zeebe.camel.ZeebeComponent
import io.zeebe.camel.ZeebeComponentContext
import io.zeebe.camel.api.command.CompleteJobCommand
import io.zeebe.camel.core.fn.JobEventCompressor
import io.zeebe.client.api.events.JobEvent
import org.apache.camel.Exchange
import org.apache.camel.Producer
import org.apache.camel.impl.DefaultProducer
import org.apache.camel.spi.UriEndpoint
import org.slf4j.LoggerFactory

@UriEndpoint(
  scheme = ZeebeComponent.SCHEME,
  title = "Zeebe Complete Job",
  syntax = JobCompleteEndpoint.ENDPOINT,
  producerOnly = true
)
class JobCompleteEndpoint(context: ZeebeComponentContext) : ZeebeProducerOnlyEndpoint(context, JobCompleteEndpoint.ENDPOINT) {

  companion object {
    const val REMAINING = "job/complete"
    const val ENDPOINT = "${ZeebeComponent.SCHEME}:$REMAINING"

    private val logger = LoggerFactory.getLogger(JobCompleteEndpoint::class.java)
  }


  override fun createProducer(): Producer = object : DefaultProducer(this) {
    override fun process(exchange: Exchange) {
      val cmd = exchange.`in`.getMandatoryBody(CompleteJobCommand::class.java)
      logger.info("received completeCommand: {}", cmd)

      val jobEvent: JobEvent = JobEventCompressor().decode(cmd.jobEvent)

      logger.info("received jobEvent: {}", jobEvent)

      val builder = context.jobClient
        .newCompleteCommand(jobEvent)

      val payload = cmd.payload

      if (payload != null) {
        builder.payload(payload)
      }

      builder.send().join()

      logger.info("completed job: {}", cmd);
    }
  }
}
