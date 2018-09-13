package io.holunda.zeebe.facebam.broker.route;

import static io.holunda.zeebe.facebam.broker.route.ZeebeRoute.Suffix.png;
import static java.util.Objects.requireNonNull;

import io.holunda.zeebe.facebam.broker.config.BrokerConfigurationProperties;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;

public abstract class ZeebeRoute extends RouteBuilder {

  public enum Suffix {
    bpmn,
    png,
    register,
    complete
  }

  protected final BrokerConfigurationProperties properties;

  protected ZeebeRoute(BrokerConfigurationProperties properties) {
    this.properties = properties;
  }

  protected String imageFromCloudInbox() {
    return "file:" + properties.getCloud().getInbox() + "?include=.*\\." + png.name() +  "$";
  }


  protected String inboxWithSuffix(Suffix suffix) {
    requireNonNull(suffix);
    return "file:" + properties.getBroker().getInbox() + "?include=.*\\." + suffix.name() +  "$";
  }


}
