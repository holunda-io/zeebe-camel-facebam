package io.zeebe.camel

import io.zeebe.camel.endpoint.*
import io.zeebe.client.ZeebeClient
import io.zeebe.client.api.clients.JobClient
import io.zeebe.client.api.clients.TopicClient
import io.zeebe.client.api.clients.WorkflowClient
import io.zeebe.client.api.events.JobEvent
import io.zeebe.client.impl.ZeebeClientImpl
import io.zeebe.client.impl.data.ZeebeObjectMapperImpl
import org.apache.camel.Endpoint
import org.apache.camel.impl.DefaultComponent
import java.util.function.Supplier

/**
 * The zeebe core component. It is identified by camel via the {@link ZeebeComponent#SCHEME}
 * keyword. To get registered, this class is bound to the scheme in <code>/META-INF/services/org/apache/camel/component/zeebe</code>.
 */
open class ZeebeComponent(private val clientSupplier: Supplier<ZeebeClient>) : DefaultComponent() {

    constructor(client: ZeebeClient) : this(Supplier { client })

    companion object {
        const val SCHEME = "zeebe"
    }

    @Throws(IllegalArgumentException::class)
    private fun createEndpoint(context: ZeebeComponentContext): Endpoint = when (context.remaining) {
        JobCompleteEndpoint.REMAINING -> JobCompleteEndpoint(context)
        ProcessDeployEndpoint.REMAINING -> ProcessDeployEndpoint(context)
        JobSubscribeEndpoint.REMAINING -> JobSubscribeEndpoint(context)
        ProcessStartEndpoint.REMAINING -> ProcessStartEndpoint(context)
        WorkerRegisterEndpoint.REMAINING -> WorkerRegisterEndpoint(context)
      TopicMonitorEndpoint.REMAINING -> TopicMonitorEndpoint(context)

        else -> throw IllegalArgumentException("unkown: ${context.remaining}")
    }

    override fun createEndpoint(uri: String, remaining: String, parameters: MutableMap<String, Any>): Endpoint = createEndpoint(ZeebeComponentContext(uri, remaining, parameters, clientSupplier))
}

/**
 * The component context stores all endpoint parameters passed via dsl and provides access to the
 * ZeebeClient functions.
 */
data class ZeebeComponentContext(
        val uri: String,
        val remaining: String,
        val parameters: Map<String, Any>,
        val clientSupplier: Supplier<ZeebeClient>
) {

    val type: String
    val command: String

    init {
        val parts: MatchResult.Destructured = """(\w+)/(\w+)""".toRegex().find(remaining)!!.destructured

        type = parts.component1()
        command = parts.component2()
    }

    val topicClient: TopicClient by lazy {
        clientSupplier.get().topicClient()
    }

    val workflowClient: WorkflowClient by lazy {
        topicClient.workflowClient()
    }

    val jobClient: JobClient by lazy {
        topicClient.jobClient()
    }

    val objectMapper: ZeebeObjectMapperImpl by lazy {
        val client = clientSupplier.get()
        if (client is ZeebeClientImpl)
            client.objectMapper
        else
            ZeebeObjectMapperImpl()
    }

    fun jobEvent(event: JobEvent) = objectMapper.toJson(event)!!
    fun jobEvent(json: String) = objectMapper.fromJson(json, JobEvent::class.java)!!
}
