rootProject.name = "zeebe-camel-facebam"

include("client")
findProject(":client")?.name = "facebam-client"

include("cloud:broker")
findProject(":cloud:broker")?.name = "facebam-broker"


arrayOf("common", "watermarker", "thumbnailer").forEach {
  include("cloud:worker:$it")
  findProject(":cloud:worker:$it")?.name = "facebam-worker-$it"
}

include("lib")
arrayOf("json").forEach {
  include("lib:$it")
  findProject(":lib:$it")?.name = "facebam-lib-$it"
}

include("lib:json")

include("lib:camel-zeebe")
arrayOf("api", "core").forEach {
  include("lib:camel-zeebe:$it")
  findProject(":lib:camel-zeebe:$it")?.name = "camel-zeebe-$it"
}
