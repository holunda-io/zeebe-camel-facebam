package io.holunda.zeebe.facebam.worker.common

import org.springframework.boot.context.properties.ConfigurationProperties


@ConfigurationProperties("facebam")
data class WorkerProperties(
        var name: String? = null,
        var worker: FS? = null,
        var broker: FS? = null
) {

  data class FS(
    var inbox:String? = null,
    var outbox:String? = null,
    var work:String? = null
  )
}

