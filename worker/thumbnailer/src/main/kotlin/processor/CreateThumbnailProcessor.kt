package io.holunda.zeebe.facebam.worker.thumbnailer.processor

import io.holunda.zeebe.facebam.lib.worker.modifyFileName
import org.apache.camel.Exchange
import org.apache.camel.Processor
import org.imgscalr.Scalr
import org.springframework.stereotype.Component
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.InputStream
import javax.imageio.ImageIO


@Component
class CreateThumbnailProcessor : Processor {

  companion object {
    const val THUMBNAIL_WIDTH = 150
    const val THUMBNAIL_HEIGHT = 150
  }

  override fun process(exchange: Exchange) {
    val sourceFileName = exchange.getIn().headers[Exchange.FILE_NAME] as String
    val thumbFileName = modifyFileName(sourceFileName, "thumb")

    val fis = exchange.getIn().getBody(InputStream::class.java)

    val typedImageStream = ImageIO.createImageInputStream(fis)
    val reader = (ImageIO.getImageReaders(typedImageStream).next()
      ?: throw IllegalArgumentException("No reader found")).apply { input = typedImageStream }

    val source = reader.read(0)
    val thumbnail = createThumbnail(source)


    val baos = ByteArrayOutputStream().use {
      ImageIO.write(thumbnail, reader.formatName, it)
      it
    }

    exchange.getIn().body = baos
    exchange.getIn().headers[Exchange.FILE_NAME] = thumbFileName
  }

  fun createThumbnail(image: BufferedImage): BufferedImage {
    return Scalr.resize(image, Scalr.Method.ULTRA_QUALITY, THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT)
  }
}
