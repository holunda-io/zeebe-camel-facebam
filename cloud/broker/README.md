# Zeebe Broker Application

The broker is the heart of the cloud infrastructure. It runs processes, notifies 
workers and keeps the state. In fact, it the only component that ever keeps state.

For the camel example, we have a zeebe-client running on the same node as the broker.
It connects to the broker and sends/receives commands and events via camel.

## Configuration

* The broker bootstraps a topic `default-topic`. This is the only topic we will use
* The client also defaults to this default topic.  

## Implementation

Dependencies:

* spring boot starter
* zeebe spring boot broker
* zeebe spring boot client
* camel spring boot 

Written in old-school java, because we can.
No web stuff, no persistence/jpa, all handled by the broker (using tmp directory)
