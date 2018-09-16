package io.zeebe.camel.endpoint

import io.zeebe.camel.ZeebeComponent
import io.zeebe.camel.ZeebeComponentContext
import io.zeebe.camel.api.command.StartProcessCommand
import org.apache.camel.Exchange
import org.apache.camel.Producer
import org.apache.camel.impl.DefaultProducer
import org.apache.camel.spi.UriEndpoint
import org.apache.camel.spi.UriParam
import org.slf4j.LoggerFactory


@UriEndpoint(
    scheme = ZeebeComponent.SCHEME,
    title = "Zeebe Start Process",
    syntax = ProcessStartEndpoint.ENDPOINT,
    producerOnly = true
)
class ProcessStartEndpoint(context: ZeebeComponentContext) : ZeebeProducerOnlyEndpoint(context, ProcessStartEndpoint.ENDPOINT) {

  companion object {
    const val REMAINING = "process/start"
    const val ENDPOINT = "${ZeebeComponent.SCHEME}:$REMAINING"
    private val logger = LoggerFactory.getLogger(ProcessStartEndpoint::class.java)!!

    @JvmStatic
    fun endpoint(debug : Boolean = false) = "$ENDPOINT?debug=$debug"
  }

  @UriParam(name = "debug", label = "log started process")
  @org.apache.camel.spi.Metadata(required = "false")
  var debug: Boolean = false

  override fun createProducer(): Producer = object : DefaultProducer(this) {
    override fun process(exchange: Exchange) {
      val cmd = exchange.getIn().getMandatoryBody(StartProcessCommand::class.java)

      val builder = context.workflowClient
        .newCreateInstanceCommand()
        .bpmnProcessId(cmd.bpmnProcessId)
        .latestVersion()

      if (cmd.payload != null) {
        builder.payload(cmd.payload)
      }

      val event = builder
        .send()
        .join()


      if (debug)
        logger.info("started: bpmnProcessId={}, {}", cmd.bpmnProcessId, zeebeObjectMapper.toJson(event) )
    }
  }
}
