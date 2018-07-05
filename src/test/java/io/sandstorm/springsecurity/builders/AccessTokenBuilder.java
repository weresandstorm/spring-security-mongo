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

package io.sandstorm.springsecurity.builders;

import static io.sandstorm.springsecurity.builders.OAuth2AccessTokenBuilder.oAuth2AccessTokenBuilder;
import static io.sandstorm.springsecurity.commons.SecurityRDG.string;

import io.sandstorm.springsecurity.domain.AccessToken;
import org.springframework.security.oauth2.common.util.SerializationUtils;

public class AccessTokenBuilder {

  private String tokenId = string().next();
  private byte[] token = SerializationUtils.serialize(oAuth2AccessTokenBuilder().build());
  private String authenticationId = string().next();
  private String username = string().next();
  private String clientId = string().next();
  private byte[] authentication =
      SerializationUtils.serialize(
          OAuth2AuthenticationBuilder.oAuth2AuthenticationBuilder().build());
  private String refreshTokenId = string().next();

  private AccessTokenBuilder() {}

  public static AccessTokenBuilder kAccessTokenBuilder() {
    return new AccessTokenBuilder();
  }

  public AccessToken build() {
    return new AccessToken(
        tokenId, token, authenticationId, username, clientId, authentication, refreshTokenId);
  }

  public AccessTokenBuilder token(final byte[] token) {
    this.token = token;
    return this;
  }
}
