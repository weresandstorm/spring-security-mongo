package me.konglong.springsecurity.builders;

import static me.konglong.springsecurity.builders.OAuth2RequestBuilder.oAuth2RequestBuilder;

import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import uk.org.fyodor.generators.RDG;

public class OAuth2AuthenticationBuilder {

  private OAuth2AuthenticationBuilder() {}

  public static OAuth2AuthenticationBuilder oAuth2AuthenticationBuilder() {
    return new OAuth2AuthenticationBuilder();
  }

  public OAuth2Authentication build() {
    return new OAuth2Authentication(
        oAuth2RequestBuilder().build(),
        new TestingAuthenticationToken(
            KUserAccountBuilder.userAccountBuilder().build(), RDG.string().next()));
  }
}
