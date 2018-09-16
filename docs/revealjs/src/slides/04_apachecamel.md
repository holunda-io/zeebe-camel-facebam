

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
* (any) transport protocol 

---

# Camel building blocks

|  Concept      | Blocks        | Transport     |
|:--------------|:------------- |:--------------|
| Component     | Consumer      | Exchange      |
| Route         | Processor     | Message       |
|               | Producer      | - Header<br/>- Body      |

---

# Route Example

```java
// subscribe to topic
from("activemq:topic?[options]")

  // call a processor-method
  .process(convertToPojo())

  // add content to exchange
  .enrich(loadAdditionalData())

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

--

## Camel Zeebe API

* immutable Commands and Events
* Marshal to json
* Self-Registration
* no dependency to zeebe

--

## Camel Zeebe Core

* Camel Component
* Endpoints for Subscription, Completion, ... 
* "Orchestrator" connected to Broker
