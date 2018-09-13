package io.holunda.zeebe.facebam.broker.route;

import static io.holunda.zeebe.facebam.broker.route.ZeebeRoute.Suffix.register;
import static org.apache.camel.LoggingLevel.INFO;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.holunda.zeebe.facebam.broker.config.BrokerConfigurationProperties;
import io.zeebe.camel.api.command.RegisterJobWorkerCommand;
import io.zeebe.camel.endpoint.WorkerRegisterEndpoint;
import io.zeebe.camel.processor.JsonProcessorsKt;
import org.springframework.stereotype.Component;

@Component
public class RegisterJobWorkerRoute extends ZeebeRoute{

  private final ObjectMapper objectMapper;

  protected RegisterJobWorkerRoute(BrokerConfigurationProperties properties, ObjectMapper objectMapper) {
    super(properties);
    this.objectMapper = objectMapper;
  }

  @Override
  public void configure() {
    from(inboxWithSuffix(register))
      .id("register worker")
      .log(INFO,"register worker, read json: ${body}")

      .process(JsonProcessorsKt.bodyFromJson(objectMapper, RegisterJobWorkerCommand.class))

      .log(INFO,"register worker, received command: ${body}")

      .to(WorkerRegisterEndpoint.ENDPOINT);
  }

}
