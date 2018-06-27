package me.konglong.springsecurity.service;

import static me.konglong.springsecurity.builders.OAuth2AccessTokenBuilder.oAuth2AccessTokenBuilder;
import static me.konglong.springsecurity.builders.KUserAccountBuilder.userAccountBuilder;
import static me.konglong.springsecurity.commons.SecurityRDG.string;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import me.konglong.springsecurity.builders.KClientTokenBuilder;
import me.konglong.springsecurity.builders.OAuth2ProtectedResourceDetailsBuilder;
import me.konglong.springsecurity.domain.KClientToken;
import me.konglong.springsecurity.domain.ClientTokenRepo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.ClientKeyGenerator;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

@RunWith(MockitoJUnitRunner.class)
public class ClientTokenServiceTest {

  @Mock private ClientTokenRepo clientTokenRepo;

  @Mock private ClientKeyGenerator keyGenerator;

  private KClientTokenService clientTokenService;

  @Before
  public void setup() {
    clientTokenService = new KClientTokenService(clientTokenRepo, keyGenerator);
  }

  @Test
  public void shouldSaveAccessToken() {
    // Given
    final OAuth2ProtectedResourceDetails oAuth2ProtectedResourceDetails =
        OAuth2ProtectedResourceDetailsBuilder.oAuth2ProtectedResourceDetailsBuilder().build();
    final TestingAuthenticationToken authentication =
        new TestingAuthenticationToken(userAccountBuilder().build(), string().next());
    final OAuth2AccessToken oAuth2AccessToken = oAuth2AccessTokenBuilder().build();

    // And
    final String authenticationId = string().next();
    given(keyGenerator.extractKey(oAuth2ProtectedResourceDetails, authentication))
        .willReturn(authenticationId);

    // When
    clientTokenService.saveAccessToken(
        oAuth2ProtectedResourceDetails, authentication, oAuth2AccessToken);

    // Then
    verify(keyGenerator, atLeastOnce()).extractKey(oAuth2ProtectedResourceDetails, authentication);
    verify(clientTokenRepo).save(any(KClientToken.class));
    verify(clientTokenRepo).deleteByAuthenticationId(authenticationId);
  }

  @Test
  public void shouldRemoveAccessToken() {
    // Given
    final OAuth2ProtectedResourceDetails oAuth2ProtectedResourceDetails =
        OAuth2ProtectedResourceDetailsBuilder.oAuth2ProtectedResourceDetailsBuilder().build();
    final TestingAuthenticationToken authentication =
        new TestingAuthenticationToken(userAccountBuilder().build(), string().next());

    // And
    final String value = string().next();
    when(keyGenerator.extractKey(oAuth2ProtectedResourceDetails, authentication)).thenReturn(value);
    // When
    clientTokenService.removeAccessToken(oAuth2ProtectedResourceDetails, authentication);

    // Then
    verify(clientTokenRepo).deleteByAuthenticationId(value);
  }

  @Test
  public void shouldGetAccessToken() {
    // Given
    final OAuth2ProtectedResourceDetails oAuth2ProtectedResourceDetails =
        OAuth2ProtectedResourceDetailsBuilder.oAuth2ProtectedResourceDetailsBuilder().build();
    final TestingAuthenticationToken authentication =
        new TestingAuthenticationToken(userAccountBuilder().build(), string().next());

    // And
    final String authenticationId = string().next();
    given(keyGenerator.extractKey(oAuth2ProtectedResourceDetails, authentication))
        .willReturn(authenticationId);

    // And
    final OAuth2AccessToken expectedToken = oAuth2AccessTokenBuilder().build();
    given(clientTokenRepo.findByAuthenticationId(authenticationId))
        .willReturn(
            KClientTokenBuilder.kClientTokenBuilder()
                .token(expectedToken)
                .build());

    // When
    final OAuth2AccessToken accessToken =
        clientTokenService.getAccessToken(oAuth2ProtectedResourceDetails, authentication);

    // Then
    assertThat(accessToken).isEqualTo(expectedToken);
  }
}
