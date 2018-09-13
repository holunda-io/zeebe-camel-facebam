package io.zeebe.camel

import io.zeebe.client.api.events.JobEvent
import io.zeebe.client.impl.ZeebeClientImpl
import io.zeebe.test.ZeebeTestRule
import org.junit.Rule
import org.junit.Test
import org.slf4j.LoggerFactory
import java.util.*
import java.util.stream.Collectors
import io.zeebe.test.TopicEventRecorder.jobType
import org.assertj.core.api.Assertions
import org.awaitility.Awaitility
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler
import org.mockito.Mockito.timeout
import org.yaml.snakeyaml.util.UriEncoder
import java.net.URLDecoder
import java.net.URLEncoder
import java.time.Duration


class PlainZeebeTest {

  val logger = LoggerFactory.getLogger(PlainZeebeTest::class.java)

  @get:Rule
  val zeebe = ZeebeTestRule()


  @Test
  fun `start and complete`() {
    deploy()
    logDeployments()

    start()

    subscribe()


    Awaitility.await().untilAsserted({ 1 > 7 })
  }

  fun deploy() {
    zeebe.client.topicClient().workflowClient()
      .newDeployCommand()
      .addResourceFromClasspath("dummy.bpmn")
      .send().join()

  }


  fun subscribe() {
zeebe.client.topicClient().jobClient()
      .newWorker()
      .jobType("doSomething")
      .handler{ c, job ->
        logger.info("hello")

        val om = (zeebe.client as ZeebeClientImpl).objectMapper

        val json = om.toJson(job)


        logger.info("json: ------------------- $json")
        val url = URLEncoder.encode(json, Charsets.UTF_8.name())
        logger.info("url:------------------- $url")
        val urlDec = URLDecoder.decode(url, Charsets.UTF_8.name())
        logger.info("urlDec: --------------- $urlDec")
        Assertions.assertThat(urlDec).isEqualTo(json)

//        val baseEnc =



        c.newCompleteCommand(om.fromJson(json, JobEvent::class.java))
          .payload("""{"bar":"17"}""")
          .send()
          .join()

        logger.info("---------------------------------- job completed")
      }
      .timeout(Duration.ofSeconds(10))
      .open()
  }

  fun logDeployments() {
    logger.info("deployments: {}", zeebe.client
      .topicClient()
      .workflowClient()
      .newWorkflowRequest()
      .send()
      .join()
      .getWorkflows()
      .stream()
      .map { wf -> wf.getBpmnProcessId() + "[" + wf.getVersion() + "]" }
      .sorted()
      .collect(Collectors.toList<String>()))
  }

  fun start() {
    zeebe.client.topicClient().workflowClient()
      .newCreateInstanceCommand()
      .bpmnProcessId("process_dummy")
      .latestVersion()
      .payload(ZeebeWorkingSpike.StartPayload(UUID.randomUUID().toString()))
      .send()
      .join();
  }
}
