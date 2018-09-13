package io.zeebe.camel.routes

import io.zeebe.camel.ZeebeComponent
import io.zeebe.camel.processor.FromFileToProcessDeployCommand
import io.zeebe.test.ZeebeTestRule
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.impl.DefaultCamelContext
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class DeployFromFileTest {

  @get:Rule
  val zeebe = ZeebeTestRule()

  @get:Rule
  val tmp = TemporaryFolder()

  @Before
  fun setUp() {
    tmp.create()
    val camel = DefaultCamelContext()
    camel.addComponent(ZeebeComponent.SCHEME, ZeebeComponent(zeebe.client))

    camel.addRoutes(object: RouteBuilder() {
      override fun configure() {
        from("file:${tmp.root.absolutePath}&noop=true") //?include=.*\\.bpmn&noop=true")
            .bean(FromFileToProcessDeployCommand)
            .to("log:out")
      }
    })

    camel.start()
  }

  @Test
  fun `deploy from file`() {
    val f = tmp.newFile("foo.bpmn")
    f.printWriter().use { out -> out.print("j") }

    Thread.sleep(10000L)

  }
}
