package io.holunda.zeebe.facebam.lib.worker

import io.zeebe.camel.api.RegisterJobWorkerGateway
import io.zeebe.camel.api.command.RegisterJobWorkerCommand
import io.zeebe.camel.api.zeebeRegisterJobWorkerGateway
import io.zeebe.camel.lib.json.toJsonFile
import org.apache.camel.CamelContext
import org.apache.camel.LoggingLevel
import org.apache.camel.builder.RouteBuilder
import org.springframework.context.annotation.Bean


class JobWorkerConfiguration {

  @Bean
  fun objectMapper() = io.zeebe.camel.lib.json.JsonProcessors.objectMapper

  @Bean
  fun registerJobWorkerGateway(camel: CamelContext) = camel.zeebeRegisterJobWorkerGateway()

  @Bean
  fun registerWorker(properties: WorkerProperties) = object : RouteBuilder() {
    override fun configure() {
      from(RegisterJobWorkerGateway.ENDPOINT)
        .id("publish-registration: ${properties.name}")
        .log(LoggingLevel.INFO, "publish registration for ${properties.name}")
        .toJsonFile(RegisterJobWorkerCommand::class,
          properties.broker!!.inbox!!,
          "${properties.key}.register")
    }
  }
}

fun RegisterJobWorkerGateway.sendRegistration(properties: WorkerProperties) = this.send(
  RegisterJobWorkerCommand(
    jobType = properties.jobType!!,
    workerName = properties.name!!,
    to = "file:${properties.worker!!.inbox!!}" +
      "?fileName=${properties.key}.job", // fixme require fields in properties
    toJson = true
  )
)


