package io.holunda.zeebe.facebam.orchestrator.route;

import io.zeebe.camel.endpoint.TopicMonitorEndpoint;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class MonitorTopicRoute extends RouteBuilder {

  @Override
  public void configure() {
    from(TopicMonitorEndpoint.ENDPOINT)
      .log(LoggingLevel.INFO, "monitor: ${body}")
      .to("spring-event://default");
  }
}
