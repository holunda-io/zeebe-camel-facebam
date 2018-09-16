package io.holunda.zeebe.facebam.lib.worker

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.zeebe.camel.api.ZippedString
import io.zeebe.camel.api.event.JobCreatedEvent
import io.zeebe.camel.lib.json.fromJsonFile
import mu.KLogging
import org.apache.camel.Exchange
import org.apache.camel.Exchange.*
import org.apache.camel.LoggingLevel.INFO
import org.apache.camel.Processor
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.model.RouteDefinition
import org.apache.camel.processor.aggregate.AggregationStrategy
import kotlin.reflect.KClass

abstract class WorkerRouteBuilder(
  val objectMapper: ObjectMapper,
  val properties: WorkerProperties
) : RouteBuilder() {

  companion object : KLogging() {
    const val HEADER_JOB_EVENT = "jobEventId"
    const val FILE_PARENT_NAME = "file:\${header.CamelFileParent}?fileName=\${header.CamelFileName}"
  }

  data class Image(val name: String, val directory: String) {
    @get:JsonIgnore
    val path by lazy {
      "$directory/$name"
    }
  }

  data class Payload(val image: Image)

  abstract fun createCompleteCommand() : Processor

  private val workerInbox by lazy {
    properties.worker!!.inbox!!
  }

  fun readJobsFromWorkerInbox(routeId: String) = fromJsonFile(
    id = routeId,
    type = JobCreatedEvent::class,
    directory = workerInbox,
    suffix = "job",
    noop = false,
    initialDelay = 5000
  )!!

  // fixme extension
  fun processor(name: String, body: (Exchange) -> Unit) = object : Processor {
    override fun process(exchange: Exchange) = body.invoke(exchange)
    override fun toString() = name
  }

  fun RouteDefinition.copyEventDataToHeader() = this.process(processor("copyEventDataToHeader") {
    val jobCreateEvent = it.`in`.getMandatoryBody(JobCreatedEvent::class.java)
    val payload = objectMapper.readValue<Payload>(jobCreateEvent.payload as String)

    with(it.`in`) {
      setHeader(HEADER_JOB_EVENT, jobCreateEvent.jobEvent)
      setHeader(JobCreatedEvent.JOB_KEY, jobCreateEvent.key)
      setHeader(FILE_PARENT, payload.image.directory)
      setHeader(FILE_NAME, payload.image.name)
    }


  })!!

  private object KeepJobEventHeader : AggregationStrategy {
    override fun aggregate(oldExchange: Exchange, newExchange: Exchange): Exchange = newExchange.apply {
      val oldHeaders = oldExchange.`in`.headers

      with(this.`in`) {
        setHeader(HEADER_JOB_EVENT, oldHeaders[HEADER_JOB_EVENT])
        setHeader(JobCreatedEvent.JOB_KEY, oldHeaders[JobCreatedEvent.JOB_KEY])
      }
    }
  }

  fun <T : Any> bodyToJson(type: KClass<T>) = processor("body-to-json")
    {
      val cmd = it.`in`.getMandatoryBody(type.java)
      val json = objectMapper.writeValueAsString(cmd)
      it.`in`.body = json
    }

  fun brokerInbox() = "file:${properties.broker?.inbox!!}?fileName=${properties.key}-\${header.${JobCreatedEvent.JOB_KEY}}.complete"


  fun RouteDefinition.loadWorkImage() = this
    .log(INFO, "trying to load workImage: $FILE_PARENT_NAME ")
    .pollEnrich()
    .simple("$FILE_PARENT_NAME&noop=true")
    .aggregationStrategy(KeepJobEventHeader)
    .log(INFO, "loaded")!!

}
