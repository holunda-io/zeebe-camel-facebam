@file:Suppress(PACKAGE)

package io.zeebe.camel.api

import org.apache.camel.CamelContext

const val PACKAGE = "PackageDirectoryMismatch"

typealias ZippedString = String
typealias Json = String
typealias Xml = String
typealias Yaml = String


fun CamelContext.zeebeDeployProcessGateway(endpoint: String = DeployProcessGateway.ENDPOINT) = io.zeebe.camel.api.deployGateway(this, endpoint)
fun CamelContext.zeebeStartProcessGateway(endpoint: String = StartProcessGateway.ENDPOINT) = io.zeebe.camel.api.startProcessGateway(this, endpoint)
fun CamelContext.zeebeRegisterJobWorkerGateway(endpoint: String = RegisterJobWorkerGateway.ENDPOINT) = io.zeebe.camel.api.registerJobWorker(this, endpoint)
