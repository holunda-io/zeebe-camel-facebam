package io.zeebe.camel.endpoint

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.zeebe.camel.ZeebeComponentContext
import io.zeebe.client.api.record.ZeebeObjectMapper
import org.apache.camel.Consumer
import org.apache.camel.Processor
import org.apache.camel.Producer
import org.apache.camel.impl.DefaultEndpoint

/**
 * Root of all zeebe endpoints.
 *
 * Defines the context in which the zeebe client and all parameters and helpers are stored.
 */
abstract class ZeebeEndpoint(
  val context: ZeebeComponentContext,
  val syntax: String,
  val objectMapper: ObjectMapper = jacksonObjectMapper()
) : DefaultEndpoint() {
  abstract override fun createConsumer(processor: Processor): Consumer
  abstract override fun createProducer(): Producer

  val zeebeObjectMapper: ZeebeObjectMapper by lazy {
    context.objectMapper
  }

  override fun isSingleton(): Boolean = true
  override fun createEndpointUri() = context.uri
}

/**
 * Consumer only endpoint.
 * Only requires the Endpoint#createConsumer method and throws an exception when used in a producer context.
 *
 * Must be annotated with UriEndpoint#consumerOnly.
 */
abstract class ZeebeConsumerOnlyEndpoint(
  context: ZeebeComponentContext,
  syntax: String
) : ZeebeEndpoint(context, syntax) {
  override fun createProducer() = throw UnsupportedOperationException("$syntax is consumerOnly!")
}

/**
 * Producer only endpoint.
 * Only requires the Endpoint#createProducer method and throws an exception when used in a consumer context.
 *
 * Must be annotated with UriEndpoint#producerOnly.
 */
abstract class ZeebeProducerOnlyEndpoint(
  context: ZeebeComponentContext,
  syntax: String
) : ZeebeEndpoint(context, syntax) {
  override fun createConsumer(processor: Processor) = throw UnsupportedOperationException("$syntax} is producerOnly!")
}
