package io.holunda.zeebe.facebam.broker.route;

import static io.holunda.zeebe.facebam.broker.route.ZeebeRoute.Suffix.complete;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.holunda.zeebe.facebam.broker.config.BrokerConfigurationProperties;
import io.zeebe.camel.api.command.CompleteJobCommand;
import io.zeebe.camel.endpoint.JobCompleteEndpoint;
import io.zeebe.camel.processor.JsonProcessorsKt;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Component
public class CompleteJobRoute extends ZeebeRoute{

  private final ObjectMapper objectMapper;
  protected CompleteJobRoute(BrokerConfigurationProperties properties, ObjectMapper objectMapper) {
    super(properties);
    this.objectMapper = objectMapper;
  }

  @Override
  public void configure() {
    from(inboxWithSuffix(complete))
      .id("complete job")
      .log(LoggingLevel.INFO, "complete job: ${body}")
      .process(JsonProcessorsKt.bodyFromJson(objectMapper, CompleteJobCommand.class))
      .process(exchange -> {
        CompleteJobCommand body = exchange.getIn()
          .getMandatoryBody(CompleteJobCommand.class);

        System.out.println("received complete body from file: " + body);
      })
      .to(JobCompleteEndpoint.ENDPOINT);
  }
}
