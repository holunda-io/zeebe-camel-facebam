package io.zeebe.camel

import com.fasterxml.jackson.databind.ObjectMapper
import io.zeebe.camel.api.*
import io.zeebe.camel.api.command.CompleteJobCommand
import io.zeebe.camel.api.command.DeployProcessCommand
import io.zeebe.camel.api.command.RegisterJobWorkerCommand
import io.zeebe.camel.api.command.StartProcessCommand
import io.zeebe.camel.api.event.JobCreatedEvent
import io.zeebe.camel.endpoint.ProcessDeployEndpoint
import io.zeebe.camel.endpoint.ProcessStartEndpoint
import io.zeebe.camel.endpoint.WorkerRegisterEndpoint
import io.zeebe.client.api.record.Record
import io.zeebe.test.ZeebeTestRule
import org.apache.camel.CamelContext
import org.apache.camel.LoggingLevel
import org.apache.camel.Processor
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.impl.DefaultCamelContext
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.slf4j.LoggerFactory
import java.util.*
import java.util.function.Supplier

@Ignore
class ZeebeWorkingSpike {

    companion object {
        val logger = LoggerFactory.getLogger(ZeebeWorkingSpike::class.java)
    }

    @get:Rule
    val zeebe = ZeebeTestRule()

    private val records = mutableListOf<Record>()

    lateinit var deployProcessGateway: DeployProcessGateway
    lateinit var startProcessGateway: StartProcessGateway
    lateinit var registerJobWorkerGateway: RegisterJobWorkerGateway

    val objectMapper = ObjectMapper()

    data class ReturnPayload(val bar: Int)
    data class StartPayload(val foo: String)


    private fun initCamel() {

        // create default context for testing
        val camel: CamelContext = DefaultCamelContext()

        // allow sending deploy commands to route
        deployProcessGateway = camel.zeebeDeployProcessGateway()
        // allow sending start commands to route
        startProcessGateway = camel.zeebeStartProcessGateway()
        registerJobWorkerGateway = camel.zeebeRegisterJobWorkerGateway()

        // register zeebe main component
        camel.addComponent(ZeebeComponent.SCHEME, ZeebeComponent(Supplier { zeebe.client }))


        // register routes
        camel.addRoutes(object : RouteBuilder() {
            override fun configure() {
                from(StartProcessGateway.ENDPOINT)
                        .id("start the process")
                        .log(LoggingLevel.INFO, "starting the process")
                        .to(ProcessStartEndpoint.ENDPOINT)

                from(DeployProcessGateway.ENDPOINT)
                        .id("deploy the process")
                        .log(LoggingLevel.INFO, "deploying the process")
                        .to(ProcessDeployEndpoint.ENDPOINT)

                from(RegisterJobWorkerGateway.ENDPOINT)
                        .id("add subscription")
                        .log(LoggingLevel.INFO, "register job worker")
                        .to(WorkerRegisterEndpoint.ENDPOINT)

        from("zeebe:job/subscribe?jobType=doSomething&workerName=dummyCompletor")
            .routeId("subscribe")
            .bean(Processor {
              val evt = it.`in`.getMandatoryBody(JobCreatedEvent::class.java)
              val payloadIn = objectMapper.convertValue(evt.payload, StartPayload::class.java)

              val payloadOut = ReturnPayload(payloadIn.foo.length)

              it.`in`.body = CompleteJobCommand(
                  jobType = evt.jobType,
                  workerName = evt.workerName,
                  payload = objectMapper.writeValueAsString(payloadOut),
                  jobEvent=  evt.jobEvent
              )
            })
            .to("zeebe:job/complete")
            }
        })

        // start the context
        camel.start()
    }

    @Test
    fun `start process and work on task`() {
        initCamel()
        subscribeLogger()

        // use proxy to send deploy command to direct:deploy
        deployProcessGateway.send(DeployProcessCommand.of("/dummy.bpmn"))

        registerJobWorkerGateway.send(RegisterJobWorkerCommand("doSomething", "dynamic", to = "file:/Users/jangalinski/msg"))

        // use proxy to send start command to direct:start
        startProcessGateway.send(
          StartProcessCommand(
            bpmnProcessId = "process_dummy",
            payload = objectMapper.writeValueAsString(StartPayload(UUID.randomUUID().toString()))
          )
        )

        // the completion is done via route zeebe:jobworker->direct:foo->zeebe:complete

//        await().untilAsserted {
//            logger.info("records: {}", records)
//            records.find { it.metadata.intent == "COMPLETED" } != null
//        }
    }


    private fun subscribeLogger() = zeebe.client.topicClient()
            .newSubscription()
            .name("record-logger")
            .recordHandler { record ->
                records += record
                logger.info(
                        """
              Record-Logger: ${record.metadata.key} ${record.metadata.valueType}
                  ${record.toJson()}
                    """
                )
            }
            .startAtHeadOfTopic()
            .forcedStart()
            .open();

}
