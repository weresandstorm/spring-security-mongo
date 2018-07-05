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

package io.sandstorm.springsecurity.service;

import static java.util.Objects.nonNull;
import static org.springframework.security.oauth2.common.util.SerializationUtils.deserialize;
import static org.springframework.security.oauth2.common.util.SerializationUtils.serialize;

import io.sandstorm.springsecurity.domain.AccessToken;
import io.sandstorm.springsecurity.domain.AccessTokenRepo;
import io.sandstorm.springsecurity.domain.RefreshToken;
import io.sandstorm.springsecurity.domain.RefreshTokenRepo;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;

@Component
public class ConcreteTokenStore implements TokenStore {

  private final AccessTokenRepo accessTokenRepo;

  private final RefreshTokenRepo refreshTokenRepo;

  private final AuthenticationKeyGenerator authenticationKeyGenerator;

  public ConcreteTokenStore(
      final AccessTokenRepo accessTokenRepo,
      final RefreshTokenRepo refreshTokenRepo,
      final AuthenticationKeyGenerator authenticationKeyGenerator) {
    this.accessTokenRepo = accessTokenRepo;
    this.refreshTokenRepo = refreshTokenRepo;
    this.authenticationKeyGenerator = authenticationKeyGenerator;
  }

  @Override
  public OAuth2Authentication readAuthentication(final OAuth2AccessToken token) {
    return readAuthentication(token.getValue());
  }

  @Override
  public OAuth2Authentication readAuthentication(final String token) {
    final String tokenId = extractTokenKey(token);

    final AccessToken AccessToken = accessTokenRepo.findByTokenId(tokenId);

    if (nonNull(AccessToken)) {
      try {
        return deserializeAuthentication(AccessToken.getAuthentication());
      } catch (IllegalArgumentException e) {
        removeAccessToken(token);
      }
    }

    return null;
  }

  @Override
  public void storeAccessToken(
      final OAuth2AccessToken token, final OAuth2Authentication authentication) {
    String refreshToken = null;
    if (nonNull(token.getRefreshToken())) {
      refreshToken = token.getRefreshToken().getValue();
    }

    if (nonNull(readAccessToken(token.getValue()))) {
      removeAccessToken(token.getValue());
    }

    final String tokenKey = extractTokenKey(token.getValue());

    final AccessToken accessToken =
        new AccessToken(
            tokenKey,
            serializeAccessToken(token),
            authenticationKeyGenerator.extractKey(authentication),
            authentication.isClientOnly() ? null : authentication.getName(),
            authentication.getOAuth2Request().getClientId(),
            serializeAuthentication(authentication),
            extractTokenKey(refreshToken));

    accessTokenRepo.save(accessToken);
  }

  @Override
  public OAuth2AccessToken readAccessToken(final String tokenValue) {
    final String tokenKey = extractTokenKey(tokenValue);
    final AccessToken AccessToken = accessTokenRepo.findByTokenId(tokenKey);
    if (nonNull(AccessToken)) {
      try {
        return deserializeAccessToken(AccessToken.getToken());
      } catch (IllegalArgumentException e) {
        removeAccessToken(tokenValue);
      }
    }
    return null;
  }

  @Override
  public void removeAccessToken(final OAuth2AccessToken token) {
    removeAccessToken(token.getValue());
  }

  @Override
  public void storeRefreshToken(
      final org.springframework.security.oauth2.common.OAuth2RefreshToken refreshToken,
      final OAuth2Authentication oAuth2Authentication) {
    final String tokenKey = extractTokenKey(refreshToken.getValue());
    final byte[] token = serializeRefreshToken(refreshToken);
    final byte[] authentication = serializeAuthentication(oAuth2Authentication);

    final RefreshToken oAuth2RefreshToken = new RefreshToken(tokenKey, token, authentication);

    refreshTokenRepo.save(oAuth2RefreshToken);
  }

  @Override
  public org.springframework.security.oauth2.common.OAuth2RefreshToken readRefreshToken(
      final String tokenValue) {
    final String tokenKey = extractTokenKey(tokenValue);
    final RefreshToken RefreshToken = refreshTokenRepo.findByTokenId(tokenKey);

    if (nonNull(RefreshToken)) {
      try {
        return deserializeRefreshToken(RefreshToken.getToken());
      } catch (IllegalArgumentException e) {
        removeRefreshToken(tokenValue);
      }
    }

    return null;
  }

  @Override
  public OAuth2Authentication readAuthenticationForRefreshToken(
      final org.springframework.security.oauth2.common.OAuth2RefreshToken token) {
    return readAuthenticationForRefreshToken(token.getValue());
  }

  @Override
  public void removeRefreshToken(
      final org.springframework.security.oauth2.common.OAuth2RefreshToken token) {
    removeRefreshToken(token.getValue());
  }

  @Override
  public void removeAccessTokenUsingRefreshToken(
      final org.springframework.security.oauth2.common.OAuth2RefreshToken refreshToken) {
    removeAccessTokenUsingRefreshToken(refreshToken.getValue());
  }

  @Override
  public OAuth2AccessToken getAccessToken(final OAuth2Authentication authentication) {
    OAuth2AccessToken accessToken = null;

    String key = authenticationKeyGenerator.extractKey(authentication);

    final AccessToken oAuth2AccessToken = accessTokenRepo.findByAuthenticationId(key);

    if (oAuth2AccessToken != null) {
      accessToken = deserializeAccessToken(oAuth2AccessToken.getToken());
    }

    if (accessToken != null
        && !key.equals(
            authenticationKeyGenerator.extractKey(readAuthentication(accessToken.getValue())))) {
      removeAccessToken(accessToken.getValue());
      // Keep the store consistent (maybe the same user is represented by this authentication but
      // the details have
      // changed)
      storeAccessToken(accessToken, authentication);
    }
    return accessToken;
  }

  @Override
  public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(
      String clientId, String userName) {
    final List<AccessToken> accessTokens =
        accessTokenRepo.findByUsernameAndClientId(userName, clientId);
    return transformToOAuth2AccessTokens(accessTokens);
  }

  @Override
  public Collection<OAuth2AccessToken> findTokensByClientId(final String clientId) {
    final List<AccessToken> accessTokens = accessTokenRepo.findByClientId(clientId);
    return transformToOAuth2AccessTokens(accessTokens);
  }

  protected String extractTokenKey(final String value) {
    if (Objects.isNull(value)) {
      return null;
    }
    MessageDigest digest;
    try {
      digest = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException(
          "MD5 algorithm not available.  Fatal (should be in the JDK).");
    }

    try {
      byte[] bytes = digest.digest(value.getBytes("UTF-8"));
      return String.format("%032x", new BigInteger(1, bytes));
    } catch (UnsupportedEncodingException e) {
      throw new IllegalStateException(
          "UTF-8 encoding not available.  Fatal (should be in the JDK).");
    }
  }

  protected byte[] serializeAccessToken(final OAuth2AccessToken token) {
    return serialize(token);
  }

  protected byte[] serializeRefreshToken(
      final org.springframework.security.oauth2.common.OAuth2RefreshToken token) {
    return serialize(token);
  }

  protected byte[] serializeAuthentication(final OAuth2Authentication authentication) {
    return serialize(authentication);
  }

  protected OAuth2AccessToken deserializeAccessToken(final byte[] token) {
    return deserialize(token);
  }

  protected org.springframework.security.oauth2.common.OAuth2RefreshToken deserializeRefreshToken(
      final byte[] token) {
    return deserialize(token);
  }

  protected OAuth2Authentication deserializeAuthentication(final byte[] authentication) {
    return deserialize(authentication);
  }

  public OAuth2Authentication readAuthenticationForRefreshToken(final String value) {
    final String tokenId = extractTokenKey(value);

    final RefreshToken RefreshToken = refreshTokenRepo.findByTokenId(tokenId);

    if (nonNull(RefreshToken)) {
      try {
        return deserializeAuthentication(RefreshToken.getAuthentication());
      } catch (IllegalArgumentException e) {
        removeRefreshToken(value);
      }
    }

    return null;
  }

  private void removeRefreshToken(final String token) {
    final String tokenId = extractTokenKey(token);
    refreshTokenRepo.deleteByTokenId(tokenId);
  }

  private void removeAccessTokenUsingRefreshToken(final String refreshToken) {
    final String tokenId = extractTokenKey(refreshToken);
    accessTokenRepo.deleteByRefreshTokenId(tokenId);
  }

  private void removeAccessToken(final String tokenValue) {
    final String tokenKey = extractTokenKey(tokenValue);
    accessTokenRepo.deleteByTokenId(tokenKey);
  }

  private Collection<OAuth2AccessToken> transformToOAuth2AccessTokens(
      final List<AccessToken> accessTokens) {
    return accessTokens
        .stream()
        .filter(Objects::nonNull)
        .map(token -> SerializationUtils.<OAuth2AccessToken>deserialize(token.getToken()))
        .collect(Collectors.toList());
  }
}
