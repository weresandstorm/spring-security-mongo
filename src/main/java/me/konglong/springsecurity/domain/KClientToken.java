package me.konglong.springsecurity.domain;

import java.util.Arrays;
import java.util.Objects;
import org.bson.codecs.pojo.annotations.BsonCreator;

public class KClientToken implements Entity<String> {

  private String id;
  private String tokenId;
  private byte[] token;
  private String authenticationId;
  private String username;
  private String clientId;

  public KClientToken() {}

  @BsonCreator
  public KClientToken(
      final String id,
      final String tokenId,
      final byte[] token,
      final String authenticationId,
      final String username,
      final String clientId) {
    this.id = id;
    this.tokenId = tokenId;
    this.token = token;
    this.authenticationId = authenticationId;
    this.username = username;
    this.clientId = clientId;
  }

  @Override
  public String id() {
    return id;
  }

  public String getTokenId() {
    return tokenId;
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

  @Override
  public int hashCode() {
    return Objects.hash(tokenId, token, authenticationId, username, clientId);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final KClientToken other = (KClientToken) obj;
    return Objects.equals(this.tokenId, other.tokenId)
        && Objects.equals(this.token, other.token)
        && Objects.equals(this.authenticationId, other.authenticationId)
        && Objects.equals(this.username, other.username)
        && Objects.equals(this.clientId, other.clientId);
  }

  @Override
  public String toString() {
    return "KClientToken{"
        + "id='"
        + id
        + '\''
        + ", tokenId='"
        + tokenId
        + '\''
        + ", token="
        + Arrays.toString(token)
        + ", authenticationId='"
        + authenticationId
        + '\''
        + ", username='"
        + username
        + '\''
        + ", clientId='"
        + clientId
        + '\''
        + '}';
  }
}
