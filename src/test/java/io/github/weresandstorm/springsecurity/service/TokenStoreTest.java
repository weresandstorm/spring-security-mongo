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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import io.github.weresandstorm.springsecurity.builders.AccessTokenBuilder;
import io.github.weresandstorm.springsecurity.builders.OAuth2RefreshTokenBuilder;
import io.github.weresandstorm.springsecurity.builders.RefreshTokenBuilder;
import io.github.weresandstorm.springsecurity.commons.SecurityRDG;
import io.github.weresandstorm.springsecurity.domain.AccessToken;
import io.github.weresandstorm.springsecurity.domain.AccessTokenRepo;
import io.github.weresandstorm.springsecurity.domain.RefreshTokenRepo;
import io.github.weresandstorm.springsecurity.builders.OAuth2AccessTokenBuilder;
import io.github.weresandstorm.springsecurity.builders.OAuth2AuthenticationBuilder;
import io.github.weresandstorm.springsecurity.domain.RefreshToken;
import java.util.Collection;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import uk.org.fyodor.generators.RDG;

@RunWith(MockitoJUnitRunner.class)
public class TokenStoreTest {

  @Mock private AccessTokenRepo accessTokenRepo;

  @Mock private RefreshTokenRepo refreshTokenRepo;

  @Mock private AuthenticationKeyGenerator authenticationKeyGenerator;

  @InjectMocks private ConcreteTokenStore tokenStore;

  @Test
  public void shouldStoreAccessToken() {
    // Given
    final org.springframework.security.oauth2.common.OAuth2AccessToken auth2AccessToken =
        OAuth2AccessTokenBuilder.oAuth2AccessTokenBuilder().build();
    final byte[] token = SerializationUtils.serialize(auth2AccessToken);

    // And
    final OAuth2Authentication oAuth2Authentication =
        OAuth2AuthenticationBuilder.oAuth2AuthenticationBuilder().build();

    // And
    given(accessTokenRepo.findByTokenId(any(String.class)))
        .willReturn(AccessTokenBuilder.kAccessTokenBuilder().token(token).build());

    // When
    tokenStore.storeAccessToken(auth2AccessToken, oAuth2Authentication);

    // Then
    verify(accessTokenRepo).deleteByTokenId(any(String.class));
    verify(accessTokenRepo).save(any(AccessToken.class));
  }

  @Test
  public void shouldReadAccessToken() {
    // Given
    final String tokenValue = RDG.string().next();

    // And
    final org.springframework.security.oauth2.common.OAuth2AccessToken auth2AccessToken =
        OAuth2AccessTokenBuilder.oAuth2AccessTokenBuilder().token(tokenValue).build();
    final byte[] token = SerializationUtils.serialize(auth2AccessToken);

    // And
    given(accessTokenRepo.findByTokenId(any(String.class)))
        .willReturn(AccessTokenBuilder.kAccessTokenBuilder().token(token).build());

    // When
    final org.springframework.security.oauth2.common.OAuth2AccessToken oAuth2AccessToken =
        tokenStore.readAccessToken(tokenValue);

    // Then
    assertThat(oAuth2AccessToken.getValue()).isEqualTo(tokenValue);
  }

  @Test
  public void shouldReturnNullWhenNoReadAccessToken() {
    // Given
    final String tokenValue = RDG.string().next();

    // And
    given(accessTokenRepo.findByTokenId(any(String.class))).willReturn(null);

    // When
    final org.springframework.security.oauth2.common.OAuth2AccessToken oAuth2AccessToken =
        tokenStore.readAccessToken(tokenValue);

    // Then
    assertThat(oAuth2AccessToken).isNull();
  }

  @Test
  public void shouldRemoveAccessToken() {
    // Given
    final org.springframework.security.oauth2.common.OAuth2AccessToken oAuth2AccessToken =
        OAuth2AccessTokenBuilder.oAuth2AccessTokenBuilder().build();

    // When
    tokenStore.removeAccessToken(oAuth2AccessToken);

    // Then
    verify(accessTokenRepo).deleteByTokenId(any(String.class));
  }

  @Test
  public void shouldStoreRefreshToken() {
    // Given
    final org.springframework.security.oauth2.common.OAuth2RefreshToken oAuth2RefreshToken =
        OAuth2RefreshTokenBuilder.oAuth2RefreshToken().build();

    // And
    final OAuth2Authentication oAuth2Authentication =
        OAuth2AuthenticationBuilder.oAuth2AuthenticationBuilder().build();

    // And
    final ArgumentCaptor<RefreshToken> argumentCaptor = ArgumentCaptor.forClass(RefreshToken.class);

    // When
    tokenStore.storeRefreshToken(oAuth2RefreshToken, oAuth2Authentication);

    // Then
    verify(refreshTokenRepo).save(argumentCaptor.capture());
    final RefreshToken refreshToken = argumentCaptor.getValue();
    final byte[] expectedResult = SerializationUtils.serialize(oAuth2RefreshToken);
    assertThat(refreshToken.getToken()).isEqualTo(expectedResult);
  }

  @Test
  public void shouldReadRefreshToken() {
    // Given
    final String tokenValue = RDG.string().next();
    final org.springframework.security.oauth2.common.OAuth2RefreshToken oAuth2RefreshToken =
        OAuth2RefreshTokenBuilder.oAuth2RefreshToken().build();
    final byte[] oAuth2RefreshTokenSer = SerializationUtils.serialize(oAuth2RefreshToken);

    // And
    given(refreshTokenRepo.findByTokenId(any(String.class)))
        .willReturn(
            RefreshTokenBuilder.kRefreshTokenBuilder().token(oAuth2RefreshTokenSer).build());

    // When
    final org.springframework.security.oauth2.common.OAuth2RefreshToken result =
        tokenStore.readRefreshToken(tokenValue);

    // Then
    assertThat(result.getValue()).isEqualTo(oAuth2RefreshToken.getValue());
  }

  @Test
  public void shouldReadNullWhenNoRefreshToken() {
    // Given
    final String tokenValue = RDG.string().next();

    // And
    given(refreshTokenRepo.findByTokenId(any(String.class))).willReturn(null);

    // When
    final org.springframework.security.oauth2.common.OAuth2RefreshToken result =
        tokenStore.readRefreshToken(tokenValue);

    // Then
    assertThat(result).isNull();
  }

  @Test
  public void shouldReadAuthenticationForRefreshToken() {
    // Given
    final org.springframework.security.oauth2.common.OAuth2RefreshToken oAuth2RefreshToken =
        OAuth2RefreshTokenBuilder.oAuth2RefreshToken().build();

    // And
    final OAuth2Authentication authentication =
        OAuth2AuthenticationBuilder.oAuth2AuthenticationBuilder().build();
    final byte[] authenticationSer = SerializationUtils.serialize(authentication);

    // And
    given(refreshTokenRepo.findByTokenId(any(String.class)))
        .willReturn(
            RefreshTokenBuilder.kRefreshTokenBuilder().authentication(authenticationSer).build());
    // When
    final OAuth2Authentication oAuth2Authentication =
        tokenStore.readAuthenticationForRefreshToken(oAuth2RefreshToken);

    // Then
    assertThat(oAuth2Authentication.getPrincipal()).isEqualTo(authentication.getPrincipal());
    assertThat(oAuth2Authentication.getCredentials()).isEqualTo(authentication.getCredentials());
  }

  @Test
  public void shouldReadNullWhenAuthenticationForNoRefreshToken() {
    // Given
    final org.springframework.security.oauth2.common.OAuth2RefreshToken oAuth2RefreshToken =
        OAuth2RefreshTokenBuilder.oAuth2RefreshToken().build();

    // And
    given(refreshTokenRepo.findByTokenId(any(String.class))).willReturn(null);
    // When
    final OAuth2Authentication oAuth2Authentication =
        tokenStore.readAuthenticationForRefreshToken(oAuth2RefreshToken);

    // Then
    assertThat(oAuth2Authentication).isNull();
  }

  @Test
  public void shouldRemoveRefreshToken() {
    // Given
    final org.springframework.security.oauth2.common.OAuth2RefreshToken oAuth2RefreshToken =
        OAuth2RefreshTokenBuilder.oAuth2RefreshToken().build();

    // When
    tokenStore.removeRefreshToken(oAuth2RefreshToken);

    // Then
    verify(refreshTokenRepo).deleteByTokenId(any(String.class));
  }

  @Test
  public void shouldRemoveAccessTokenUsingRefreshToken() {
    // Given
    final org.springframework.security.oauth2.common.OAuth2RefreshToken oAuth2RefreshToken =
        OAuth2RefreshTokenBuilder.oAuth2RefreshToken().build();

    // When
    tokenStore.removeAccessTokenUsingRefreshToken(oAuth2RefreshToken);

    // Then
    verify(accessTokenRepo).deleteByRefreshTokenId(any(String.class));
  }

  @Test
  public void shouldGetAccessToken() {
    // Given
    final OAuth2Authentication oAuth2Authentication =
        OAuth2AuthenticationBuilder.oAuth2AuthenticationBuilder().build();

    // And
    final String value = RDG.string().next();
    doReturn(value).doReturn(value).when(authenticationKeyGenerator).extractKey(any());

    // And
    final org.springframework.security.oauth2.common.OAuth2AccessToken oAuth2AccessToken =
        OAuth2AccessTokenBuilder.oAuth2AccessTokenBuilder().build();

    final byte[] oAuth2AccessTokenSer = SerializationUtils.serialize(oAuth2AccessToken);
    given(accessTokenRepo.findByAuthenticationId(value))
        .willReturn(AccessTokenBuilder.kAccessTokenBuilder().token(oAuth2AccessTokenSer).build());

    // And
    given(accessTokenRepo.findByTokenId(any()))
        .willReturn(AccessTokenBuilder.kAccessTokenBuilder().build());

    // When
    tokenStore.getAccessToken(oAuth2Authentication);

    // Then
    verify(accessTokenRepo, never()).deleteByTokenId(any(String.class));
    verify(accessTokenRepo, never()).save(any(AccessToken.class));
  }

  @Test
  public void shouldReturnNullWhenNoAccessToken() {
    // Given
    final OAuth2Authentication oAuth2Authentication =
        OAuth2AuthenticationBuilder.oAuth2AuthenticationBuilder().build();

    // And
    final String value = RDG.string().next();
    given(authenticationKeyGenerator.extractKey(oAuth2Authentication)).willReturn(value);

    // And
    given(accessTokenRepo.findByAuthenticationId(value)).willReturn(null);

    // When
    tokenStore.getAccessToken(oAuth2Authentication);

    // Then
    verify(accessTokenRepo, never()).deleteByTokenId(any(String.class));
    verify(accessTokenRepo, never()).save(any(AccessToken.class));
    verify(accessTokenRepo, never()).findByTokenId(anyString());
  }

  @Test
  public void shouldGetAccessTokenAndRemoveOldTokenAndPersistNewOne() {
    // Given
    final OAuth2Authentication oAuth2Authentication =
        OAuth2AuthenticationBuilder.oAuth2AuthenticationBuilder().build();

    // And
    final String value = RDG.string().next();
    given(authenticationKeyGenerator.extractKey(oAuth2Authentication)).willReturn(value);

    // And
    given(accessTokenRepo.findByAuthenticationId(value))
        .willReturn(AccessTokenBuilder.kAccessTokenBuilder().build());

    // And
    given(accessTokenRepo.findByTokenId(anyString()))
        .willReturn(AccessTokenBuilder.kAccessTokenBuilder().build());

    // When
    tokenStore.getAccessToken(oAuth2Authentication);

    // Then
    verify(accessTokenRepo, atLeastOnce()).deleteByTokenId(any(String.class));
    verify(accessTokenRepo).save(any(AccessToken.class));
  }

  @Test
  public void shouldFindTokensByClientIdAndUserName() {
    // Given
    final String username = RDG.string().next();
    final String clientId = RDG.string().next();

    // And
    final List<AccessToken> expectedTokens =
        RDG.list(SecurityRDG.ofMongoOAuth2AccessToken()).next();
    given(accessTokenRepo.findByUsernameAndClientId(username, clientId)).willReturn(expectedTokens);

    // When
    final Collection<org.springframework.security.oauth2.common.OAuth2AccessToken> tokens =
        tokenStore.findTokensByClientIdAndUserName(clientId, username);

    // Then
    assertThat(tokens).hasSize(expectedTokens.size());
  }

  @Test
  public void shouldFindTokensByClientId() {
    // Given
    final String clientId = RDG.string().next();

    // And
    final List<AccessToken> expectedTokens =
        RDG.list(SecurityRDG.ofMongoOAuth2AccessToken()).next();
    given(accessTokenRepo.findByClientId(clientId)).willReturn(expectedTokens);

    // When
    final Collection<org.springframework.security.oauth2.common.OAuth2AccessToken> tokens =
        tokenStore.findTokensByClientId(clientId);

    // Then
    assertThat(tokens).hasSize(expectedTokens.size());
  }
}
