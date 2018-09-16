package io.holunda.zeebe.facebam.orchestrator.route;

import static java.util.Collections.singletonMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.holunda.zeebe.facebam.orchestrator.config.OrchestratorConfigurationProperties;
import io.zeebe.camel.api.command.StartProcessCommand;
import io.zeebe.camel.endpoint.ProcessStartEndpoint;
import java.util.HashMap;
import java.util.Map;
import org.apache.camel.component.file.GenericFileMessage;
import org.springframework.stereotype.Component;

@Component
public class StartProcessRoute extends ZeebeRoute {

  public static final String PROCESS_ID = "image_processing";
  private final ObjectMapper objectMapper;

  protected StartProcessRoute(OrchestratorConfigurationProperties properties, ObjectMapper objectMapper) {
    super(properties);
    this.objectMapper = objectMapper;
  }

  @Override
  public void configure() {
    from(imageFromCloudInbox() + "&initialDelay=5000")
      .id("start-process-when-image-is-uploaded")

      .to("file:" + properties.getCloud().getWork()).id("copy-to-work-dir")

      .process(exchange -> {
        final GenericFileMessage msg = exchange.getIn(GenericFileMessage.class);

        final Map<String, Object> originalImage = new HashMap<>();
        originalImage.put("name", msg.getGenericFile().getFileName());
        originalImage.put("directory", properties.getCloud().getWork());

        StartProcessCommand cmd = new StartProcessCommand(
          PROCESS_ID,
          objectMapper.writeValueAsString(
            singletonMap(
              "originalImage", originalImage)
          )
        );

        exchange.getIn().setBody(cmd);
      }).id("create-start-command")

      .to(ProcessStartEndpoint.endpoint(true)).id("notify-worker")
    ;
  }
}
