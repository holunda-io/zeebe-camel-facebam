package io.holunda.zeebe.facebam.lib.worker

import org.springframework.boot.context.properties.ConfigurationProperties


@ConfigurationProperties("facebam")
data class WorkerProperties(
  var jobType: String? = null,
  var name: String? = null,
  var worker: FS? = null,
  var broker: FS? = null
) {

  val key by lazy {
    "$name-$jobType"
  }

  data class FS(
    var inbox: String? = null,
    var outbox: String? = null,
    var work: String? = null
  )
}

