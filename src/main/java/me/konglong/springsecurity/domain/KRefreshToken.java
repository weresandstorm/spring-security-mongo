package me.konglong.springsecurity.domain;

import org.bson.codecs.pojo.annotations.BsonCreator;

public class KRefreshToken implements Entity<String> {

  private String id;
  private byte[] token;
  private byte[] authentication;

  public KRefreshToken() {}

  @BsonCreator
  public KRefreshToken(final String id, final byte[] token, final byte[] authentication) {
    this.id = id;
    this.token = token;
    this.authentication = authentication;
  }

  @Override
  public String id() {
    return id;
  }

  public byte[] getToken() {
    return token;
  }

  public byte[] getAuthentication() {
    return authentication;
  }
}
