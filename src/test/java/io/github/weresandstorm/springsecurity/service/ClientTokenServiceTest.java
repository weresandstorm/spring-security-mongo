/*
 * Copyright (C) 2018 The Sandstorm Org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.weresandstorm.springsecurity.service;

import static io.github.weresandstorm.springsecurity.builders.OAuth2AccessTokenBuilder.oAuth2AccessTokenBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.weresandstorm.springsecurity.builders.ClientTokenBuilder;
import io.github.weresandstorm.springsecurity.builders.UserIdentityBuilder;
import io.github.weresandstorm.springsecurity.domain.ClientToken;
import io.github.weresandstorm.springsecurity.domain.ClientTokenRepo;
import io.github.weresandstorm.springsecurity.builders.OAuth2ProtectedResourceDetailsBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.ClientKeyGenerator;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import uk.org.fyodor.generators.RDG;

@RunWith(MockitoJUnitRunner.class)
public class ClientTokenServiceTest {

  @Mock private ClientTokenRepo clientTokenRepo;

  @Mock private ClientKeyGenerator keyGenerator;

  private ConcreteClientTokenService clientTokenService;

  @Before
  public void setup() {
    clientTokenService = new ConcreteClientTokenService(clientTokenRepo, keyGenerator);
  }

  @Test
  public void shouldSaveAccessToken() {
    // Given
    final OAuth2ProtectedResourceDetails oAuth2ProtectedResourceDetails =
        OAuth2ProtectedResourceDetailsBuilder.oAuth2ProtectedResourceDetailsBuilder().build();
    final TestingAuthenticationToken authentication =
        new TestingAuthenticationToken(
            UserIdentityBuilder.userIdentityBuilder().build(), RDG.string().next());
    final OAuth2AccessToken oAuth2AccessToken = oAuth2AccessTokenBuilder().build();

    // And
    final String authenticationId = RDG.string().next();
    given(keyGenerator.extractKey(oAuth2ProtectedResourceDetails, authentication))
        .willReturn(authenticationId);

    // When
    clientTokenService.saveAccessToken(
        oAuth2ProtectedResourceDetails, authentication, oAuth2AccessToken);

    // Then
    verify(keyGenerator, atLeastOnce()).extractKey(oAuth2ProtectedResourceDetails, authentication);
    verify(clientTokenRepo).save(any(ClientToken.class));
    verify(clientTokenRepo).deleteByAuthenticationId(authenticationId);
  }

  @Test
  public void shouldRemoveAccessToken() {
    // Given
    final OAuth2ProtectedResourceDetails oAuth2ProtectedResourceDetails =
        OAuth2ProtectedResourceDetailsBuilder.oAuth2ProtectedResourceDetailsBuilder().build();
    final TestingAuthenticationToken authentication =
        new TestingAuthenticationToken(
            UserIdentityBuilder.userIdentityBuilder().build(), RDG.string().next());

    // And
    final String value = RDG.string().next();
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
        new TestingAuthenticationToken(
            UserIdentityBuilder.userIdentityBuilder().build(), RDG.string().next());

    // And
    final String authenticationId = RDG.string().next();
    given(keyGenerator.extractKey(oAuth2ProtectedResourceDetails, authentication))
        .willReturn(authenticationId);

    // And
    final OAuth2AccessToken expectedToken = oAuth2AccessTokenBuilder().build();
    given(clientTokenRepo.findByAuthenticationId(authenticationId))
        .willReturn(ClientTokenBuilder.kClientTokenBuilder().token(expectedToken).build());

    // When
    final OAuth2AccessToken accessToken =
        clientTokenService.getAccessToken(oAuth2ProtectedResourceDetails, authentication);

    // Then
    assertThat(accessToken).isEqualTo(expectedToken);
  }
}
