# The client application

The client resides outside of "the cloud".
It does not know that there is a zeebe broker involved and it does not know about the individual workers.

The client wants the job to get done:

* put an image to a folder
  * upload this image to "the cloud"
* find modified image(s) in the output directory.


## Implementation

* dependency only to camel
  * not even camel-zeebe-api, client does not start jobs or registers subscriptions.

* written in kotlin 1.2.61
