rootProject.name = "zeebe-camel-facebam"

include("client")
findProject(":client")?.name = "facebam-client"

include("cloud:broker")
findProject(":cloud:broker")?.name = "facebam-broker"


arrayOf("common", "watermarker", "thumbnailer").forEach {
    include("cloud:worker:$it")
    findProject(":cloud:worker:$it")?.name = "facebam-worker-$it"
}
