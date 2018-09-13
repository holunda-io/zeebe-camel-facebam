package io.holunda.zeebe.facebam.broker.route;

import static io.holunda.zeebe.facebam.broker.route.ZeebeRoute.Suffix.bpmn;

import io.holunda.zeebe.facebam.broker.config.BrokerConfigurationProperties;
import io.zeebe.camel.endpoint.ProcessDeployEndpoint;
import io.zeebe.camel.processor.FromFileToProcessDeployCommand;
import org.apache.camel.LoggingLevel;
import org.springframework.stereotype.Component;

@Component
public class DeployProcessRoute extends ZeebeRoute {

  public DeployProcessRoute(BrokerConfigurationProperties properties) {
    super(properties);
  }

  @Override
  public void configure() {
    from(inboxWithSuffix(bpmn))
      .id("deploy process").log(LoggingLevel.INFO, "deploying process")

      .process(FromFileToProcessDeployCommand.INSTANCE)

      .to(ProcessDeployEndpoint.endpoint(true));
  }
}
