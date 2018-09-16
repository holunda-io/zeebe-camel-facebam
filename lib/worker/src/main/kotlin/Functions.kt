package io.holunda.zeebe.facebam.lib.worker

import java.lang.IllegalArgumentException

/**
 * Usage: modifyFileName("image.jpg", "thumb") -> "image-thumb.jpg"
 */
fun modifyFileName(fileName: String, suffix: String = "") : String {
  for (type in arrayOf("png","jpg","jpeg")) {
    if (fileName.endsWith(".$type", true)) {
      val replace = if ("" == suffix) ".$type" else "-$suffix.$type"
      return fileName.replace(".$type", replace, true)
    }
  }

  throw IllegalArgumentException("unsupported file type")
}
