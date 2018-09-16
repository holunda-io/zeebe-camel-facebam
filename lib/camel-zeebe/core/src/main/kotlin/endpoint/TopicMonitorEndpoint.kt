package io.zeebe.camel.endpoint

import io.zeebe.camel.ZeebeComponent
import io.zeebe.camel.ZeebeComponentContext
import io.zeebe.client.api.subscription.TopicSubscription
import io.zeebe.client.impl.ZeebeClientImpl
import org.apache.camel.Consumer
import org.apache.camel.Processor
import org.apache.camel.impl.DefaultConsumer
import org.apache.camel.spi.UriEndpoint


@UriEndpoint(
  scheme = ZeebeComponent.SCHEME,
  title = "Zeebe Topic Monitor",
  syntax = TopicMonitorEndpoint.ENDPOINT,
  consumerOnly = true
)
class TopicMonitorEndpoint(context: ZeebeComponentContext) : ZeebeConsumerOnlyEndpoint(context, TopicMonitorEndpoint.ENDPOINT) {

  companion object {
    const val REMAINING = "topic/monitor"
    const val ENDPOINT = "${ZeebeComponent.SCHEME}:$REMAINING"
  }

  override fun createConsumer(processor: Processor): Consumer = object : DefaultConsumer(this, processor) {

    lateinit var subscription: TopicSubscription

    override fun doStart() {

      Thread.sleep(5000)

      val client = context.clientSupplier.get()
      subscription = client.topicClient(client.configuration.defaultTopic) // fixme topic configurable (or remove)
        .newSubscription()
        .name("record-logger") // fixme: make name configurable
        .recordHandler { processor.process(createExchange().apply { this.`in`.body = it }) }
        .startAtHeadOfTopic()
        .forcedStart()
        .open()

    }

    override fun doStop() {
      if (!subscription.isClosed)
        subscription.close()
    }
  }
}
