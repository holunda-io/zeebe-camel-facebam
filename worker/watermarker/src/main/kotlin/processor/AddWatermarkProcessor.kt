package io.holunda.zeebe.facebam.worker.watermarker.processor

import io.holunda.zeebe.facebam.worker.watermarker.WatermarkerApplication
import mu.KLogging
import org.apache.camel.Exchange
import org.apache.camel.Processor
import org.imgscalr.Scalr
import org.springframework.stereotype.Component
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.InputStream
import javax.imageio.ImageIO

/**
 * Represents the logo png file read from classpath resource.
 */
object ZeebeLogo {
  private const val ratio = 3
  private val logo: BufferedImage = ImageIO.read(WatermarkerApplication::class.java.getResourceAsStream("/zeebe-logo.png"))

  private fun newDimension(sourceWidth: Int): Pair<Int, Int> {
    val width = sourceWidth / ratio
    val scalingFactor = width.toDouble() / logo.width
    val height = (logo.height * scalingFactor).toInt()

    return width to height
  }

  fun resized(sourceWidth: Int): BufferedImage {
    val (width, height) = newDimension(sourceWidth)
    return Scalr.resize(logo, Scalr.Method.ULTRA_QUALITY, width, height)
  }

}

/**
 * Extend image with function to add watermark.
 */
fun BufferedImage.addWatermark() {
  val graphics = this.graphics as Graphics2D

  val resizedWatermark = ZeebeLogo.resized(this.width)
  graphics.drawImage(
    resizedWatermark,
    this.width - resizedWatermark.width,
    this.height - resizedWatermark.height,
    null)
}

@Component
class WatermarkerProcessor : Processor {
  companion object : KLogging()

  override fun process(exchange: Exchange) {
    val fis = exchange.getIn().getMandatoryBody(InputStream::class.java)
    val typedImageStream = ImageIO.createImageInputStream(fis)


    val reader = (ImageIO.getImageReaders(typedImageStream).next()
      ?: throw IllegalArgumentException("No reader found")).apply { input = typedImageStream }

    val source = reader.read(0)
    source.addWatermark()


    val baos = ByteArrayOutputStream().use {
      ImageIO.write(source, reader.formatName, it)
      it
    }

    exchange.getIn().body = baos
    exchange.getIn().headers[Exchange.FILE_NAME] = "watermarked_${exchange.getIn().headers[Exchange.FILE_NAME]}"
  }

  override fun toString() = "WatermarkerProcessor()"
}
