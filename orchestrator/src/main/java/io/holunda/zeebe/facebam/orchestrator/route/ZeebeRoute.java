package io.holunda.zeebe.facebam.orchestrator.route;

import static io.holunda.zeebe.facebam.orchestrator.route.ZeebeRoute.Suffix.jpg;
import static io.holunda.zeebe.facebam.orchestrator.route.ZeebeRoute.Suffix.png;
import static java.util.Objects.requireNonNull;

import io.holunda.zeebe.facebam.orchestrator.config.OrchestratorConfigurationProperties;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.camel.builder.RouteBuilder;

public abstract class ZeebeRoute extends RouteBuilder {

  public enum Suffix {
    bpmn,
    png,
    jpg,
    register,
    complete;

    public static String antInclude(Suffix... suffixes) {
      return "antInclude=" + Stream.of(suffixes).map(s -> "*." + s).collect(Collectors.joining(","));
    }
  }

  protected final OrchestratorConfigurationProperties properties;

  protected ZeebeRoute(OrchestratorConfigurationProperties properties) {
    this.properties = properties;
  }

  protected String imageFromCloudInbox() {
    return "file:" + properties.getCloud().getInbox()
      + "?include=.*\\.jpg$";
  }

  protected String inboxWithSuffix(Suffix suffix) {
    requireNonNull(suffix);
    return "file:" + properties.getBroker().getInbox() + "?include=.*\\." + suffix.name() +  "$";
  }


}
