@file:Suppress(PACKAGE)
package io.zeebe.camel.api.event

import io.zeebe.camel.api.Json
import io.zeebe.camel.api.PACKAGE
import io.zeebe.camel.api.ZippedString

data class JobCreatedEvent(
    val jobType: String,
    val workerName: String,
    val jobEvent: ZippedString,
    val payload: Json?
)
