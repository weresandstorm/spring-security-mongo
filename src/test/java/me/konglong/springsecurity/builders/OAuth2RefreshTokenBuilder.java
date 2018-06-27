package me.konglong.springsecurity.builders;

import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import uk.org.fyodor.generators.RDG;

public class OAuth2RefreshTokenBuilder {

  private String value = RDG.string().next();

  private OAuth2RefreshTokenBuilder() {}

  public static OAuth2RefreshTokenBuilder oAuth2RefreshToken() {
    return new OAuth2RefreshTokenBuilder();
  }

  public OAuth2RefreshTokenBuilder value(String value) {
    this.value = value;
    return this;
  }

  public OAuth2RefreshToken build() {
    return new DefaultOAuth2RefreshToken(value);
  }
}
