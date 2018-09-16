@file:Suppress(PACKAGE)
package io.zeebe.camel.api

import io.zeebe.camel.api.command.DeployProcessCommand
import io.zeebe.camel.api.command.RegisterJobWorkerCommand
import io.zeebe.camel.api.command.StartProcessCommand
import org.apache.camel.CamelContext
import org.apache.camel.builder.ProxyBuilder


interface DeployProcessGateway {
    companion object {
        const val ENDPOINT = "direct:deployProcess"
    }
  fun send(processCommand: DeployProcessCommand)
}

interface StartProcessGateway {
    companion object {
        const val ENDPOINT = "direct:startProcess"
    }
  fun send(command: StartProcessCommand)
}

interface RegisterJobWorkerGateway {
    companion object {
        const val ENDPOINT = "direct:registerJobWorker"
    }
  fun send(command: RegisterJobWorkerCommand)
}


fun deployGateway(camel: CamelContext, endpoint: String = DeployProcessGateway.ENDPOINT) : DeployProcessGateway =  ProxyBuilder(camel)
        .endpoint(endpoint)
        .build(DeployProcessGateway::class.java)

fun startProcessGateway(camel: CamelContext, endpoint: String = StartProcessGateway.ENDPOINT) : StartProcessGateway =  ProxyBuilder(camel)
        .endpoint(endpoint)
        .build(StartProcessGateway::class.java)

fun registerJobWorker(camel: CamelContext, endpoint: String = RegisterJobWorkerGateway.ENDPOINT) : RegisterJobWorkerGateway =  ProxyBuilder(camel)
        .endpoint(endpoint)
        .build(RegisterJobWorkerGateway::class.java)
