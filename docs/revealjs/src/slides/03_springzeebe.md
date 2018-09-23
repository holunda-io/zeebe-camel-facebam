

![img](images/logo-springboot.png) <!-- .element: style="width:700px" -->

> "Spring on steroids"

<!-- .slide: data-background="#EEE" -->

http://start.spring.io

---

# spring boot

* developed by pivotal
* current version `2.0.5` (2018/09)
* annotation based
* opinionated - convention over configuration
* "starter" concept
* executable jars

--

# Hello World!

```java
@SpringBootApplication
@RestController
public class MyApplication {
  public static void main(String... args) {
    SpringApplication.run(MyApplication.class, args);
  }
  
  @GetMapping("/")
  public String hello() {
    return "hello World";
  }
}

```

`mvn spring-boot:run`

---

# spring-zeebe

* "Requested" by Bernd 
* First Release End 2017
* can run broker and clients
* yaml based configuration 
* allows annotation based subscriptions
* current version: `0.3.0-SNAPSHOT`

https://github.com/zeebe-io/spring-zeebe

--

# Example: zeebe client job worker

````java
public class JobWorkerCreator {
  public static void main(String[] args) {
    final ZeebeClientBuilder builder = ZeebeClient.newClientBuilder()....;

    try (ZeebeClient client = builder.build()) {
      final JobClient jobClient = client.topicClient(topic).jobClient();

      System.out.println("Opening job worker.");

      final JobWorker workerRegistration =
          jobClient
              .newWorker()
              .jobType("sayHello")
              .handler((jobClient,job) -> jobClient.newCompleteCommand(job)
                    .payload("{\"hello\": \"world\"}")
                    .send().join())
              .open();
    }
  }
````

--

# Example: spring-zeebe-starter 

```java
@SpringBootApplication
@EnableZeebeClient
public class WorkerApplication {

  public static  void main(final String... args) {
    SpringApplication.run(WorkerApplication.class, args);
  }

  @ZeebeWorker(taskType = "sayHello")
  public void sayHello(final JobClient client, final JobEvent job) {
    client.newCompleteCommand(job)
    .payload("{\"hello\": \"world\"}")
    .send().join();
  }
}
```
