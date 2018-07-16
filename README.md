# aws facebam

Take the [zb-facebam](https://github.com/zeebe-io/zb-facebam) example to the next level using AWS, SpringBoot and Apache camel
Example setup using zeebe with spring boot and apache camel in "the cloud".

Preparing the Demo on CamundaCon 2018.

## Scope

* Deploy zeebe broker to aws 
* deploy zeebe client workers to aws
* connect via aws simple notification service
* run zb-facebam image processing example

1. upload image to bucket
2. workers are notified to add watermark and change size
3. image is written to bucket
