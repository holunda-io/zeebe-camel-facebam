@file:Suppress(PACKAGE)
package io.zeebe.camel.api

import io.zeebe.camel.api.command.CompleteJobCommand
import io.zeebe.camel.api.event.JobCreatedEvent


interface JobWorker {
  fun apply(event: JobCreatedEvent) : CompleteJobCommand
}

class JobWorkerProcessor()
