@file:Suppress(PACKAGE)

package io.zeebe.camel.api.command

import com.fasterxml.jackson.annotation.JsonIgnore
import io.zeebe.camel.api.Json
import io.zeebe.camel.api.PACKAGE
import io.zeebe.camel.api.Xml
import io.zeebe.camel.api.ZippedString
import java.io.File

data class DeployProcessCommand(
  val name: String,
  val xml: Xml
) {
  companion object {
    fun of(resourcePath: String) = with(DeployProcessCommand::class.java.getResource(resourcePath)) {
      DeployProcessCommand(
        File(file).name,
        readText().trim()
      )
    }
  }
}


data class StartProcessCommand(
  val bpmnProcessId: String,
  val payload: Json? = null
)

data class CompleteJobCommand(
  val jobType: String,
  val workerName: String,
  val jobEvent: ZippedString,
  val payload: Json? = null
)

data class RegisterJobWorkerCommand(
  val jobType: String,
  val workerName: String,
  val to: String,
  val toJson: Boolean = false
) {
  @JsonIgnore
  fun getId() = "$workerName-$jobType"
}
