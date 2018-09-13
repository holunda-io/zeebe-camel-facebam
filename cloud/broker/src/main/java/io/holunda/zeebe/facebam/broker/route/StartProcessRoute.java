package io.holunda.zeebe.facebam.broker.route;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.holunda.zeebe.facebam.broker.config.BrokerConfigurationProperties;
import io.zeebe.camel.api.command.StartProcessCommand;
import io.zeebe.camel.endpoint.ProcessStartEndpoint;
import java.util.HashMap;
import java.util.Map;
import org.apache.camel.component.file.GenericFileMessage;
import org.springframework.stereotype.Component;

@Component
public class StartProcessRoute extends ZeebeRoute {

  protected StartProcessRoute(BrokerConfigurationProperties properties) {
    super(properties);
  }

  @Override
  public void configure() throws Exception {
    from(imageFromCloudInbox() + "&initialDelay=5000")
      .process(exchange -> {
        GenericFileMessage msg = exchange.getIn(GenericFileMessage.class);

        final Map<String, Object> payload = new HashMap<>();
        payload.put("fileName", msg.getGenericFile().getFileNameOnly());
        payload.put("image", msg.getGenericFile().getAbsoluteFilePath());

        String p = new ObjectMapper().writeValueAsString(payload);

        StartProcessCommand<Map<String, String>> cmd = new StartProcessCommand(
          "image_processing",
          payload);

        exchange.getIn().setBody(cmd);
      })
      .to(ProcessStartEndpoint.endpoint(true));

  }
}
