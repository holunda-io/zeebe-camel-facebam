package io.holunda.zeebe.facebam.broker;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.holunda.zeebe.facebam.broker.config.BrokerConfigurationProperties;
import io.zeebe.camel.api.command.RegisterJobWorkerCommand;
import io.zeebe.camel.api.command.StartProcessCommand;
import io.zeebe.camel.endpoint.ProcessDeployEndpoint;
import io.zeebe.camel.endpoint.ProcessStartEndpoint;
import io.zeebe.camel.endpoint.WorkerRegisterEndpoint;
import io.zeebe.camel.processor.FromFileToProcessDeployCommand;
import io.zeebe.client.ZeebeClient;
import io.zeebe.spring.broker.EnableZeebeBroker;
import io.zeebe.spring.client.EnableZeebeClient;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.file.GenericFileMessage;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

@SpringBootApplication
@EnableZeebeBroker
@EnableZeebeClient
@EnableConfigurationProperties(BrokerConfigurationProperties.class)
public class BrokerApplication {

  private static final String PROCESS_ID = "image_processing";
  private static Logger logger = LoggerFactory.getLogger(BrokerApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(BrokerApplication.class, args);
  }

  @Autowired
  private BrokerConfigurationProperties properties;

  @Autowired
  private CamelContext camel;

  @Autowired
  @Lazy
  private ZeebeClient client;


  @Bean
  RouteBuilder deployProcess() {
    return new RouteBuilder() {

      @Override
      public void configure() {
        from("file:" + properties.getBroker().getInbox() + "?include=.*\\.bpmn$&delete=false")
          .id("deploy process")
          .log(LoggingLevel.INFO, "deploying process")
          .process(FromFileToProcessDeployCommand.INSTANCE)
          .to(ProcessDeployEndpoint.endpoint(true))
        ;
      }
    };
  }

  @Bean
  RouteBuilder registerWorker() {
    return new RouteBuilder() {
      @Override
      public void configure() throws Exception {
        from("file:" + properties.getBroker().getInbox() + "?initialDelay=10000")
          .unmarshal().json(JsonLibrary.Jackson, RegisterJobWorkerCommand.class)
          .to(WorkerRegisterEndpoint.ENDPOINT);
      }
    };
  }

  @Bean
  RouteBuilder startProcess() {
    return new RouteBuilder() {

      @Override
      public void configure() {
        from("file:" + properties.getCloud().getInbox() + "?include=.*\\.png$&initialDelay=5000")
          .to("file:" + properties.getCloud() + "/foo")
          .process(exchange -> {
            GenericFileMessage msg = exchange.getIn(GenericFileMessage.class);
            byte[] imageMime = msg.getBody(byte[].class);

            final Map<String,Object> payload = new HashMap<>();
            payload.put("fileName", msg.getGenericFile().getFileNameOnly());
            payload.put("image", msg.getGenericFile().getAbsoluteFilePath());

            String p = new ObjectMapper().writeValueAsString(payload);

            StartProcessCommand<Map<String,String>> cmd = new StartProcessCommand(PROCESS_ID, payload);

            exchange.getIn().setBody(cmd);
          })
          .to(ProcessStartEndpoint.endpoint(true));

      }
//        from("file:" + properties.getCloud().getInbox() + "?include=.*\\.png$&initialDelay=5000")
//          .id("start process")
//          .log(LoggingLevel.INFO, "start process")
//          .marshal().base64(76,"\r\n", true)
//
//          .process(exchange -> {
//            GenericFileMessage msg = exchange.getIn(GenericFileMessage.class);
//            byte[] imageMime = msg.getBody(byte[].class);
//
//            final Map<String,Object> payload = new HashMap<>();
//            payload.put("fileName", msg.getGenericFile().getFileNameOnly());
//            payload.put("image", imageMime);
//
//            String p = new ObjectMapper().writeValueAsString(payload);
//
//            StartProcessCommand<Map<String,String>> cmd = new StartProcessCommand(PROCESS_ID, payload);
//
//            exchange.getIn().setBody(cmd);
//          })
//          .to(ProcessStartEndpoint.endpoint(true));
//      }
    };
  }


  Supplier<List<String>> deployedProcesses() {
    return () -> client
      .topicClient()
      .workflowClient()
      .newWorkflowRequest()
      .send()
      .join()
      .getWorkflows()
      .stream()
      .map(wf -> wf.getBpmnProcessId() + "[" + wf.getVersion() + "]")
      .sorted()
      .collect(Collectors.toList());
  }

  @Bean
  CommandLineRunner onStart(CamelContext camelContext) {

    return args -> {
      logger.info(".... starting with properties: " + properties);
      logger.info(".... camel-components: " + camelContext.getComponentNames());
      logger.info(".... deployed-processes: {}", deployedProcesses().get());
    };
  }
}
