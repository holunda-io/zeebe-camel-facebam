package io.holunda.zeebe.facebam.client

import com.google.api.services.gmail.model.Message
import com.google.api.services.gmail.model.MessagePartBody
import mu.KLogging
import org.apache.camel.Exchange
import org.apache.camel.LoggingLevel
import org.apache.camel.builder.RouteBuilder
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

fun main(args: Array<String>) = runApplication<ClientApplication>(*args).let { Unit }

@SpringBootApplication
@EnableConfigurationProperties(ClientProperties::class)
class ClientApplication {

  companion object : KLogging()

  /**
   * Reads from client directory and copies to cloud inbox
   */
  @Bean
  fun uploadImage(properties: ClientProperties, messageState: MessageState) = object : RouteBuilder() {

    val gmailOptions = "userId=${properties.googleMail.userId}" +
      "&clientId=${properties.googleMail.clientId}" +
      "&clientSecret=${properties.googleMail.clientSecret}" +
      "&accessToken=${properties.googleMail.accessToken}" +
      "&refreshToken=${properties.googleMail.refreshToken}"

    override fun configure() {

      from("""google-mail://messages/list?$gmailOptions""")
        .id("list-messages")
        .split(body().method("messages"))
        .log(LoggingLevel.INFO, "\${body}")
        .to("""direct:get-message""")

      from("direct:get-message")
        .id("get-message")
        .log(LoggingLevel.INFO, "\${body}")
        .setHeader("gmailMessageId", simple("\${in.body.id}"))
        .filter { !messageState.seen.contains(it.message.getHeader("gmailMessageId") as String) }
        .process { messageState.seen.add(it.message.getHeader("gmailMessageId") as String) }
        .process { log.info(messageState.seen.toString()) }
        .toD("google-mail://messages/get?id=\${in.body.id}&$gmailOptions")
        .to("""direct:filter-attachments""")

      from("direct:filter-attachments")
        .id("filter-attachments")
        .process { it ->
          val googleMessage = it.message.body as Message
          val attachmentIds = googleMessage.payload.parts
            .filter { part -> part.mimeType.startsWith("image/") }
            .map { part -> AttachmentMetadata(it.message.messageId, part.body.attachmentId, part.mimeType, part.filename) }
          it.`in`.body = attachmentIds
        }
        .split()
        .body()
        .log(LoggingLevel.INFO, "\${body}")
        .to("direct:attachment-retrieve")

      from("""direct:attachment-retrieve""")
        .id("attachment-retrieve")
        .setHeader("metadata", simple("\${in.body}"))
        .setHeader(Exchange.FILE_NAME, simple("\${in.body.fileName}"))
        .toD("google-mail://attachments/get?id=\${in.body.attachmentId}&messageId=\${in.body.messageId}&$gmailOptions")
        .process {
          it.`in`.body = (it.`in`.mandatoryBody as MessagePartBody).data
        }
        .unmarshal().base64()
        .to("""file:/tmp/""")

      from("""file:${properties.client.outbox}?include=.*\.png$""")
        .id("upload-image-to-cloud")
        .log(LoggingLevel.INFO, "uploading image")
        .to("""file:${properties.cloud.inbox}""")

      from("""file:${properties.client.outbox}?include=.*\.[png|jpg]$""")
          .id("upload-image-to-cloud")
          .log(LoggingLevel.INFO, "uploading image")
          .to("""file:${properties.cloud.inbox}""")
    }
  }

  @Bean
  fun onStart(properties: ClientProperties) = CommandLineRunner {
    logger.info { ".... starting with properties: $properties" }
  }

  @Bean
  fun seenMessagesState(): MessageState = MessageState()

}

@ConfigurationProperties("facebam")
data class ClientProperties(
  val client: FS = FS(),
  val cloud: FS = FS(),
  val googleMail: GoogleMail = GoogleMail()
) {

  data class FS(
    var inbox: String? = null,
    var outbox: String? = null
  )

  data class GoogleMail(
    var clientId: String? = null,
    var clientSecret: String? = null,
    var accessToken: String? = null,
    var refreshToken: String? = null,
    var userId: String? = null

  )
}

data class AttachmentMetadata(
  val messageId: String,
  val attachmentId: String,
  val mimeType: String,
  val fileName: String
)

data class MessageState(
  val seen: MutableCollection<String> = mutableSetOf()
)
