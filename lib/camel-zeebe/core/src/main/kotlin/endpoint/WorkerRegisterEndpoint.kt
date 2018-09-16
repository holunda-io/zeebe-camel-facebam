package io.zeebe.camel.endpoint

import io.zeebe.camel.ZeebeComponent
import io.zeebe.camel.ZeebeComponentContext
import io.zeebe.camel.api.command.RegisterJobWorkerCommand
import io.zeebe.camel.api.event.JobCreatedEvent
import io.zeebe.camel.processor.bodyToJson
import org.apache.camel.Exchange
import org.apache.camel.Producer
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.impl.DefaultProducer
import org.apache.camel.spi.UriEndpoint
import org.slf4j.LoggerFactory

@UriEndpoint(
  scheme = ZeebeComponent.SCHEME,
  title = "Zeebe register worker",
  syntax = WorkerRegisterEndpoint.ENDPOINT,
  producerOnly = true
)
class WorkerRegisterEndpoint(context: ZeebeComponentContext) : ZeebeProducerOnlyEndpoint(context, WorkerRegisterEndpoint.ENDPOINT) {

  companion object {
    const val REMAINING = "worker/register"
    const val ENDPOINT = "${ZeebeComponent.SCHEME}:$REMAINING"


    private val logger = LoggerFactory.getLogger(WorkerRegisterEndpoint::class.java)
  }

  override fun createProducer(): Producer = object : DefaultProducer(this) {
    override fun process(exchange: Exchange) {
      val cmd = exchange.`in`.getMandatoryBody(RegisterJobWorkerCommand::class.java)

      exchange.context.addRoutes(object : RouteBuilder() {
        override fun configure() {
          val def = from(JobSubscribeEndpoint.endpoint(cmd.jobType, cmd.workerName))
            .id("register-worker-${cmd.getId()}")
            .process {
              val evt = it.`in`.getMandatoryBody(JobCreatedEvent::class.java)
              it.`in`.setHeader(JobCreatedEvent.JOB_KEY, evt.key)
            }


          if (cmd.toJson) {
            def.process(objectMapper.bodyToJson(JobCreatedEvent::class.java))
          }

          def.to(cmd.to)
        }
      })

      logger.info("registered worker: {}", cmd);
    }
  }
}
