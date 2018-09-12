package io.holunda.zeebe.facebam.worker.common

import org.springframework.boot.context.properties.ConfigurationProperties


@ConfigurationProperties("facebam.worker")
data class WorkerProperties(
        var name: String? = null,
        var inbox: String? = null,
        var work: String? = null
) {
    init {
        assert(name != null, { "worker.name not set" })
        assert(inbox != null, { "worker.inbox not set" })
        assert(work != null, { "work.work not set" })
    }
}
