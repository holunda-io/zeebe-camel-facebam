package io.holunda.zeebe.facebam.broker;

import io.holunda.zeebe.facebam.broker.config.BrokerConfigurationProperties;
import io.zeebe.camel.endpoint.ProcessDeployEndpoint;
import io.zeebe.camel.processor.FromFileToProcessDeployCommand;
import io.zeebe.spring.broker.EnableZeebeBroker;
import io.zeebe.spring.client.EnableZeebeClient;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableZeebeBroker
@EnableZeebeClient
@EnableConfigurationProperties(BrokerConfigurationProperties.class)
public class BrokerApplication {

  private static Logger logger = LoggerFactory.getLogger(BrokerApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(BrokerApplication.class, args);
  }

  @Autowired
  private BrokerConfigurationProperties properties;

  @Bean
  RouteBuilder deployProcess() {
    return new RouteBuilder() {

      @Override
      public void configure() {
        from("file:" + properties.getBroker().getInbox() + "?include=.*\\.bpmn$")
            .id("deploy process")
            .log(LoggingLevel.INFO, "deploying process")
            .process(FromFileToProcessDeployCommand.INSTANCE)
            .to(ProcessDeployEndpoint.ENDPOINT);
      }
    };
  }

  @Bean
  RouteBuilder startProcess() {
    return new RouteBuilder() {

      @Override
      public void configure() {
        from("file:" + properties.getCloud().getInbox() + "?include=.*\\.png$")
            .id("start process")
            .log(LoggingLevel.INFO, "start process")
            .marshal().base64()

            .process(exchange -> {

            })
          .unmarshal().base64()
            .to("file:" + properties.getBroker().getInbox());
            //.to(ProcessDeployEndpoint.ENDPOINT);
      }
    };
  }

  @Bean
  CommandLineRunner onStart(CamelContext camelContext) {

    return args -> {
      logger.info(".... starting with properties: " + properties);
      logger.info(".... camel-components: " + camelContext.getComponentNames());
    };
  }
}
