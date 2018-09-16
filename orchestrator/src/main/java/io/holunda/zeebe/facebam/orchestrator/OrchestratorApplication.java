package io.holunda.zeebe.facebam.orchestrator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.kotlin.KotlinModule;
import io.holunda.zeebe.facebam.orchestrator.config.OrchestratorConfigurationProperties;
import io.holunda.zeebe.facebam.orchestrator.query.DeployedProcessesQuery;
import io.zeebe.client.ZeebeClient;
import io.zeebe.spring.broker.EnableZeebeBroker;
import io.zeebe.spring.client.EnableZeebeClient;
import org.apache.camel.CamelContext;
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
@EnableConfigurationProperties(OrchestratorConfigurationProperties.class)
public class OrchestratorApplication {

  private static final String PROCESS_ID = "image_processing";
  private static Logger logger = LoggerFactory.getLogger(OrchestratorApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(OrchestratorApplication.class, args);
  }

  @Autowired
  private OrchestratorConfigurationProperties properties;

  @Autowired
  private CamelContext camel;

  @Autowired
  @Lazy
  private ZeebeClient client;


  @Bean
  ObjectMapper objectMapper() {
    return new ObjectMapper().registerModule(new KotlinModule());
  }


  @Bean
  CommandLineRunner onStart(CamelContext camelContext, DeployedProcessesQuery deployedProcessesQuery) {

    return args -> {
      logger.info(".... starting with properties: " + properties);
      logger.info(".... camel-components: " + camelContext.getComponentNames());
      logger.info(".... deployed-processes: {}", deployedProcessesQuery.get());
    };
  }
}
