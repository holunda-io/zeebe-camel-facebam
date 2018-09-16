

![img](images/logo-camel.png) <!-- .element: style="width:700px" -->

> Route based enterprise integration framework

http://camel.apache.org/

<!-- .slide: data-background="#EEE" -->

---

# Apache Camel

* Apache Foundation Open Source
* First commit 2007 (!)
* Implements Enterprise Integration Patterns
* 200+ production ready components
* (almost) any transport protocol 

--

![img](images/camel-overview.png) <!-- .element: style="width:700px" -->

--

<small>Camel building blocks</small>

# A Route

* defines a data flow
* starts with an **Endpoint**
* can have (multiple) **Processor**s modifying data
* ends with an **Endpoint**

--

<small>Camel building blocks</small>

# An Endpoint

Is either a ...

* **Consumer** - starts a route, creates an **Exchange**

or a

* **Producer** - ends a route, exposes **Exchange** 

--

<small>Camel building blocks</small>

# A Component

* provides a name-space (`file://folder`) 
* is a factory for **Endpoint**s

--

<small>Camel building blocks</small>

# An Exchange

* represents data in a **route**
* encloses **Message**s (In (and (Out))
* Messages have a Header (Map) and a Body

--

# Route Example

```java
// subscribe to topic
from("activemq:topic?[options]")

  .filter(doSomeFiltering())

  // call a processor-method
  .process(convertToPojo())

  // process input using spring service
  .bean(TheBusinessService.class)

  // write csv
  .marshal().csv()

  // store result on server
  .to("ftp:host?[options]")
```

---

# Camel Zeebe

> work in progress

https://github.com/holunda-io/camel-zeebe

<!-- .slide: data-background="#EEE" -->

---

# Motivation

* Do not assume that worker can connect to broker via tcp
* do not require worker to speak "zeebe"
* support any worker language
* support any messaging protocol


--

## Camel Zeebe API

* immutable Commands and Events
* no dependency on zeebe-lib
* Supports
  * Register Self
  * Start Process
  * Complete Job
  * ...

--

## Camel Zeebe API

```kotlin
data class StartProcessCommand(
  val bpmnProcessId: String,
  val payload: Json? = null
)

interface StartProcessGateway {
  companion object { const val ENDPOINT = "direct:startProcess" }
  fun send(command: StartProcessCommand)
}

startProcessGateway.send(StartProcessCommand("process_dummy", <payloadJson>))

```

--

## Camel Zeebe Core

* Zeebe Client connected to Broker
* Camel Component defining `zeebe://...`
* Provides Endpoints to 
  * Register Worker
  * Start Process
  * Complete Job
  * ...

--

## Camel Zeebe Core

```kotlin
class ProcessStartEndpoint(context: ZeebeComponentContext) : ZeebeProducerOnlyEndpoint(...) {

  override fun createProducer(): Producer = object : DefaultProducer(this) {
    override fun process(exchange: Exchange) {
      val cmd = exchange.getIn().getMandatoryBody(StartProcessCommand::class.java)

      context.workflowClient
        .newCreateInstanceCommand()
        .bpmnProcessId(cmd.bpmnProcessId)
        .latestVersion()
        .payload(cmd.payload)
        .send()
        .join()
    }
  }
}


```
