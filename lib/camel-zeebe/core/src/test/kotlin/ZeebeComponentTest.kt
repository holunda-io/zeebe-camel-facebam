package io.zeebe.camel

import io.zeebe.camel.endpoint.JobCompleteEndpoint
import io.zeebe.camel.endpoint.JobSubscribeEndpoint
import io.zeebe.camel.endpoint.ProcessDeployEndpoint
import io.zeebe.camel.endpoint.ProcessStartEndpoint
import io.zeebe.client.ZeebeClient
import org.apache.camel.impl.DefaultCamelContext
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.mockito.Answers
import org.mockito.Mockito


class ZeebeComponentTest {

  private val camel = DefaultCamelContext()
  private val client = Mockito.mock(ZeebeClient::class.java, Answers.RETURNS_DEEP_STUBS)

  init {
    camel.addComponent(ZeebeComponent.SCHEME, ZeebeComponent(client))
  }

  @Test
  fun `create job-subscribe endpoint`() {
    var endpoint = camel.getEndpoint("zeebe:job/subscribe?jobType=the-job&workerName=dummy")

    assertThat(endpoint).isInstanceOf(JobSubscribeEndpoint::class.java)
    endpoint = endpoint as JobSubscribeEndpoint

    assertThat(endpoint.workerName).isEqualTo("dummy")
    assertThat(endpoint.jobType).isEqualTo("the-job")

    endpoint = camel.getEndpoint("zeebe:job/subscribe?jobType=the-job&workerName=name", JobSubscribeEndpoint::class.java)
    assertThat(endpoint.workerName).isEqualTo("name")
  }

  @Test
  fun `create job-complete endpoint`() {
    var endpoint = camel.getEndpoint("zeebe:job/complete")
    assertThat(endpoint).isInstanceOf(JobCompleteEndpoint::class.java)
  }

  @Test
  fun `create process-deploy endpoint`() {
    var endpoint = camel.getEndpoint("zeebe:process/deploy")
    assertThat(endpoint).isInstanceOf(ProcessDeployEndpoint::class.java)
  }

  @Test
  fun `create process-start endpoint`() {
    var endpoint = camel.getEndpoint("zeebe:process/start")
    assertThat(endpoint).isInstanceOf(ProcessStartEndpoint::class.java)
  }

  @After
  fun tearDown() {
    camel.stop()
  }
}
