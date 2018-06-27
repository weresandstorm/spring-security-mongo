package me.konglong.springsecurity.domain;

import java.util.Arrays;
import java.util.Objects;
import org.bson.codecs.pojo.annotations.BsonCreator;

public class KAccessToken implements Entity<String> {

  private String id;
  private byte[] token;
  private String refreshTokenId;
  private String username;
  private String clientId;
  private String authenticationId;
  private byte[] authentication;

  public KAccessToken() {}

  @BsonCreator
  public KAccessToken(
      final String id,
      final byte[] token,
      final String authenticationId,
      final String username,
      final String clientId,
      final byte[] authentication,
      final String refreshTokenId) {
    this.id = id;
    this.token = token;
    this.authenticationId = authenticationId;
    this.username = username;
    this.clientId = clientId;
    this.authentication = authentication;
    this.refreshTokenId = refreshTokenId;
  }

  @Override
  public String id() {
    return id;
  }

  public String getTokenId() {
    return id();
  }

  public byte[] getToken() {
    return token;
  }

  public String getAuthenticationId() {
    return authenticationId;
  }

  public String getUsername() {
    return username;
  }

  public String getClientId() {
    return clientId;
  }

  public byte[] getAuthentication() {
    return authentication;
  }

  public String getRefreshTokenId() {
    return refreshTokenId;
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        token, authenticationId, username, clientId, authentication, refreshTokenId);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final KAccessToken other = (KAccessToken) obj;
    return Objects.equals(this.token, other.token)
        && Objects.equals(this.authenticationId, other.authenticationId)
        && Objects.equals(this.username, other.username)
        && Objects.equals(this.clientId, other.clientId)
        && Objects.equals(this.authentication, other.authentication)
        && Objects.equals(this.refreshTokenId, other.refreshTokenId);
  }

  @Override
  public String toString() {
    return "KAccessToken{"
        + "id='"
        + id
        + "'"
        + ", token="
        + Arrays.toString(token)
        + ", authenticationId='"
        + authenticationId
        + "'"
        + ", username='"
        + username
        + "'"
        + ", clientId='"
        + clientId
        + "'"
        + ", authentication="
        + Arrays.toString(authentication)
        + ", refreshTokenId='"
        + refreshTokenId
        + "'"
        + "}";
  }
}
