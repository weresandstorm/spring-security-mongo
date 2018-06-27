package me.konglong.springsecurity.builders;

import static me.konglong.springsecurity.commons.SecurityRDG.string;

import me.konglong.springsecurity.domain.KClientToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.util.SerializationUtils;

public class KClientTokenBuilder {

  private String id = string().next();
  private String tokenId = string().next();
  private byte[] token =
      SerializationUtils.serialize(OAuth2AccessTokenBuilder.oAuth2AccessTokenBuilder().build());
  private String authenticationId = string().next();
  private String username = string().next();
  private String clientId = string().next();

  private KClientTokenBuilder() {}

  public static KClientTokenBuilder kClientTokenBuilder() {
    return new KClientTokenBuilder();
  }

  public KClientToken build() {
    return new KClientToken(id, tokenId, token, authenticationId, username, clientId);
  }

  public KClientTokenBuilder token(final OAuth2AccessToken token) {
    this.token = SerializationUtils.serialize(token);
    return this;
  }
}
