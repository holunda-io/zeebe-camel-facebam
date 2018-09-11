package io.holunda.zeebe.facebam;

import io.zeebe.spring.broker.EnableZeebeBroker;
import io.zeebe.spring.client.EnableZeebeClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableZeebeBroker
@EnableZeebeClient
public class BrokerApplication {

  public static void main(String[] args) {
    SpringApplication.run(BrokerApplication.class, args);
  }
}
