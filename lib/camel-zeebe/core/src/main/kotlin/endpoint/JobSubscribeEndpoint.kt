package io.zeebe.camel.endpoint

import io.zeebe.camel.ZeebeComponent
import io.zeebe.camel.ZeebeComponentContext
import io.zeebe.camel.api.event.JobCreatedEvent
import io.zeebe.camel.core.fn.JobEventCompressor
import io.zeebe.camel.jobEventToJson
import io.zeebe.client.api.events.JobEvent
import io.zeebe.client.api.subscription.JobWorker
import org.apache.camel.Consumer
import org.apache.camel.Processor
import org.apache.camel.impl.DefaultConsumer
import org.apache.camel.spi.UriEndpoint
import org.apache.camel.spi.UriParam
import org.slf4j.LoggerFactory
import java.net.URLEncoder
import java.time.Duration
import java.time.temporal.ChronoUnit

@UriEndpoint(
  scheme = ZeebeComponent.SCHEME,
  title = "Zeebe Subscribe JobWorker",
  syntax = JobSubscribeEndpoint.ENDPOINT,
  consumerOnly = true
)
class JobSubscribeEndpoint(context: ZeebeComponentContext) : ZeebeConsumerOnlyEndpoint(context, JobSubscribeEndpoint.ENDPOINT) {

  companion object {
    const val REMAINING = "job/subscribe"
    const val ENDPOINT = "${ZeebeComponent.SCHEME}:$REMAINING"

    fun endpoint(jobType: String, workerName: String) = ENDPOINT +
      "?jobType=$jobType" +
      "&workerName=$workerName"

    private val logger = LoggerFactory.getLogger(JobSubscribeEndpoint::class.java)
  }


  @UriParam(name = "jobType", label = "the type of job to subscribe to")
  @org.apache.camel.spi.Metadata(required = "true")
  lateinit var jobType: String

  @UriParam(name = "workerName", label = "the name of the worker")
  @org.apache.camel.spi.Metadata(required = "false")
  lateinit var workerName: String

  override fun createConsumer(processor: Processor): Consumer = object : DefaultConsumer(this, processor) {

    lateinit var jobWorker: JobWorker

    override fun doStart() {
      jobWorker = context.jobClient
        .newWorker()
        .jobType(jobType)
        .handler { _, job -> createExchange(job) }
        .name(workerName)
        // FIXME: time out must be configurable
        .timeout(Duration.of(10, ChronoUnit.MINUTES))
        .open()

    }

    fun createExchange(job: JobEvent) {
      val exchange = endpoint.createExchange()
      val cmd = JobCreatedEvent(
        jobEvent = JobEventCompressor().encode(job),
        jobType = jobType,
        workerName = workerName,
        payload = job.payload
      )
      exchange.`in`.body = cmd

      logger.info("created job: {}", exchange.`in`.body)

      processor.process(exchange)
    }

    override fun doStop() {
      if (!jobWorker.isClosed)
        jobWorker.close()
    }
  }
}
