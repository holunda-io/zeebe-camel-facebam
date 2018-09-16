

![img](images/logo-springboot.png) <!-- .element: style="width:800px" -->

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

* "Requested" by Bernd (Summer 2017)
* can run broker and clients
* yaml basedconfiguration 
* allows annotation based subscriptions
* current version: `0.3.0-SNAPSHOT`

https://github.com/zeebe-io/spring-zeebe

--

# spring-zeebe-starter Example

```java
@SpringBootApplication
@EnableZeebeClient
public class WorkerApplication {

  public static void main(final String... args) {
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
