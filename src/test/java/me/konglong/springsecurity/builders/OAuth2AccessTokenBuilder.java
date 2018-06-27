package me.konglong.springsecurity.builders;

import static me.konglong.springsecurity.commons.SecurityRDG.localDateTime;
import static me.konglong.springsecurity.commons.SecurityRDG.objectOf;
import static me.konglong.springsecurity.util.LocalDateTimeUtil.convertToDateFrom;
import static uk.org.fyodor.generators.RDG.map;
import static uk.org.fyodor.generators.RDG.set;
import static uk.org.fyodor.generators.RDG.string;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;

public class OAuth2AccessTokenBuilder {

  private LocalDateTime expiration = localDateTime().next();
  private OAuth2RefreshToken oAuth2RefreshToken =
      OAuth2RefreshTokenBuilder.oAuth2RefreshToken().build();
  private Set<String> scope = set(string()).next();
  private Map<String, Object> additionalInformation = map(string(), objectOf(string())).next();
  private String token = string().next();

  private OAuth2AccessTokenBuilder() {}

  public static OAuth2AccessTokenBuilder oAuth2AccessTokenBuilder() {
    return new OAuth2AccessTokenBuilder();
  }

  public OAuth2AccessToken build() {
    final DefaultOAuth2AccessToken oAuth2AccessToken = new DefaultOAuth2AccessToken(token);
    oAuth2AccessToken.setExpiration(convertToDateFrom(expiration));
    oAuth2AccessToken.setRefreshToken(oAuth2RefreshToken);
    oAuth2AccessToken.setScope(scope);
    oAuth2AccessToken.setAdditionalInformation(additionalInformation);
    return oAuth2AccessToken;
  }

  public OAuth2AccessTokenBuilder token(String token) {
    this.token = token;
    return this;
  }
}
