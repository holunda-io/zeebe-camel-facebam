rootProject.name = "zeebe-camel-facebam"

include("client")
findProject(":client")?.name = "facebam-client"

include("orchestrator")
findProject(":orchestrator")?.name = "facebam-cloud-orchestrator"


arrayOf("watermarker", "thumbnailer").forEach {
  include("worker:$it")
  findProject(":worker:$it")?.name = "facebam-cloud-$it"
}

include("lib")
arrayOf("json", "worker").forEach {
  include("lib:$it")
  findProject(":lib:$it")?.name = "facebam-lib-$it"
}

include("lib:json")

include("lib:camel-zeebe")
arrayOf("api", "core").forEach {
  include("lib:camel-zeebe:$it")
  findProject(":lib:camel-zeebe:$it")?.name = "camel-zeebe-$it"
}
