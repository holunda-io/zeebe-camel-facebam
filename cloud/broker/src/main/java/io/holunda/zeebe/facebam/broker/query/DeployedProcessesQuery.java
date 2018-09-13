package io.holunda.zeebe.facebam.broker.query;

import io.zeebe.client.ZeebeClient;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class DeployedProcessesQuery implements Supplier<List<String>>  {

  private final ZeebeClient client;

  public DeployedProcessesQuery(ZeebeClient client) {
    this.client = client;
  }

  @Override
  public List<String> get() {
    return client
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
}
