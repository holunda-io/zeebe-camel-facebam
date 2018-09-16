package io.holunda.zeebe.facebam.lib.worker

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class FunctionsKtTest {

  @Test
  fun `modify filename png with watermarked`() {
    assertThat(modifyFileName("kermit.png", "watermarked")).isEqualTo("kermit-watermarked.png")
  }

  @Test
  fun `modify filename jpg with watermarked`() {
    assertThat(modifyFileName("kermit.jpg", "watermarked")).isEqualTo("kermit-watermarked.jpg")
  }

  @Test
  fun `modify filename jpeg with watermarked`() {
    assertThat(modifyFileName("kermit.JPEG", "watermarked")).isEqualTo("kermit-watermarked.jpeg")
  }

  @Test
  fun `keep if no suffix`() {
    assertThat(modifyFileName("kermit.JPEG" )).isEqualTo("kermit.jpeg")
  }
}
