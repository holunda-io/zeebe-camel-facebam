rootProject.name = "zeebe-camel-facebam"

include("client")

include("cloud:broker")

include("cloud:worker:watermarker")
include("cloud:worker:thumbnailer")
