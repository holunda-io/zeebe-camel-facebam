package io.holunda.zeebe.facebam.orchestrator.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties(prefix = "facebam")
public class OrchestratorConfigurationProperties {

  @NestedConfigurationProperty
  private final I client = new I();

  @NestedConfigurationProperty
  private final IWO cloud = new IWO();

  @NestedConfigurationProperty
  private final I broker = new I();

  private boolean keepBpmn;

  public I getClient() {
    return client;
  }

  public IWO getCloud() {
    return cloud;
  }

  public I getBroker() {
    return broker;
  }

  public boolean isKeepBpmn() {
    return keepBpmn;
  }

  public void setKeepBpmn(boolean keepBpmn) {
    this.keepBpmn = keepBpmn;
  }

  @Override
  public String toString() {
    return "BrokerConfigurationProperties{" +
      "client=" + client +
      ", cloud=" + cloud +
      ", broker=" + broker +
      ", keepBpmn=" + keepBpmn +
      '}';
  }

  public static class I {
    protected String inbox;

    public String getInbox() {
      return inbox;
    }

    public void setInbox(String inbox) {
      this.inbox = inbox;
    }

    @Override
    public String toString() {
      return "I{" +
        "inbox='" + inbox + '\'' +
        '}';
    }
  }

  public static class IW extends I {
    protected String work;

    public String getWork() {
      return work;
    }

    public void setWork(String work) {
      this.work = work;
    }

    @Override
    public String toString() {
      return "IW{" +
        "inbox='" + inbox + '\'' +
        ", work='" + work + '\'' +
        '}';
    }
  }

  public static class IWO extends IW {
    protected String outbox;

    public String getOutbox() {
      return outbox;
    }

    public void setOutbox(String outbox) {
      this.outbox = outbox;
    }

    @Override
    public String toString() {
      return "IWO{" +
        "inbox='" + inbox + '\'' +
        ", work='" + work + '\'' +
        ", outbox='" + outbox + '\'' +
        '}';
    }
  }

}
