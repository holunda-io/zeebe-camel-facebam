package io.holunda.zeebe.facebam.broker.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties(prefix = "facebam")
public class BrokerConfigurationProperties {

  @NestedConfigurationProperty
  private final FS client = new FS();

  @NestedConfigurationProperty
  private final FS cloud = new FS();

  @NestedConfigurationProperty
  private final FS broker = new FS();

  public static class FS {

    private String inbox;

    public String getInbox() {
      return inbox;
    }

    public void setInbox(String inbox) {
      this.inbox = inbox;
    }

    @Override
    public String toString() {
      return "FS{" +
          "inbox='" + inbox + '\'' +
          '}';
    }
  }

  public FS getClient() {
    return client;
  }


  public FS getCloud() {
    return cloud;
  }

  public FS getBroker() {
    return broker;
  }

  @Override
  public String toString() {
    return "BrokerConfigurationProperties{" +
        "client=" + client +
        ", cloud=" + cloud +
        ", broker=" + broker +
        '}';
  }
}
