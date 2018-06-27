package me.konglong.springsecurity.builders;

import static me.konglong.springsecurity.builders.OAuth2AccessTokenBuilder.oAuth2AccessTokenBuilder;
import static me.konglong.springsecurity.commons.SecurityRDG.string;

import me.konglong.springsecurity.domain.KAccessToken;
import org.springframework.security.oauth2.common.util.SerializationUtils;

public class KAccessTokenBuilder {

  private String tokenId = string().next();
  private byte[] token = SerializationUtils.serialize(oAuth2AccessTokenBuilder().build());
  private String authenticationId = string().next();
  private String username = string().next();
  private String clientId = string().next();
  private byte[] authentication =
      SerializationUtils.serialize(
          OAuth2AuthenticationBuilder.oAuth2AuthenticationBuilder().build());
  private String refreshTokenId = string().next();

  private KAccessTokenBuilder() {}

  public static KAccessTokenBuilder kAccessTokenBuilder() {
    return new KAccessTokenBuilder();
  }

  public KAccessToken build() {
    return new KAccessToken(
        tokenId, token, authenticationId, username, clientId, authentication, refreshTokenId);
  }

  public KAccessTokenBuilder token(final byte[] token) {
    this.token = token;
    return this;
  }
}
