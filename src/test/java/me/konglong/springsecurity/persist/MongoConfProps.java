package me.konglong.springsecurity.persist;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mongo")
public class MongoConfProps {

  public String host;

  public int port;

  public String database;

  public String username;

  public String password;

  // Note: setters must be present for populating this bean's fields.

  public void setHost(String host) {
    this.host = host;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public void setDatabase(String database) {
    this.database = database;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
