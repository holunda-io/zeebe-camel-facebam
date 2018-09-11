# The cloud

This is the cloud. Referred to hereafter as "the cloud".

Its three systems loosely coupled via camel routes.
They could run independent in docker containers, but docker is so 2017, so we have 
them as pure old fashioned system jobs with exchange through file-io.

## Components

* The Broker - keeping everything together
* The Watermarker - keen to put stamps on every image
* The Thumbnailer - shrink, shrink

