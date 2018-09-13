@file:Suppress(PACKAGE)

package io.zeebe.camel.core.fn

import io.zeebe.camel.api.PACKAGE
import io.zeebe.client.api.events.JobEvent
import io.zeebe.client.impl.data.ZeebeObjectMapperImpl
import org.junit.Test

class JobEventCompressorTest {

  val objectMapper = ZeebeObjectMapperImpl()
  val compressor = JobEventCompressor(objectMapper)

  val event = """
    {"metadata":{"intent":"ACTIVATED","valueType":"JOB","recordType":"EVENT","rejectionType":null,"topicName":"default-topic","partitionId":1,"key":4294969624,"position":4294970408,"sourceRecordPosition":4294970408,"timestamp":"2018-09-13T16:18:09.180Z","rejectionReason":null},"headers":{"activityId":"task_doSomething","workflowKey":1,"workflowInstanceKey":4294968040,"bpmnProcessId":"process_dummy","activityInstanceKey":4294969080,"workflowDefinitionVersion":1},"customHeaders":{},"deadline":"2018-09-13T16:18:19.180Z","worker":"default","retries":3,"type":"doSomething","payload":{"bar":"b3c10d67-7fc2-41f4-af0b-67ad1524538d"}}
  """.trimIndent()

  @Test
  fun `encode - decode`() {
    val baseEvent = objectMapper.fromJson(event, JobEvent::class.java)

    val zipped = compressor.encode(baseEvent)

    println("zipped: $zipped")

    val jobEvent = compressor.decode(zipped)

    println("job: $jobEvent")


  }
}
