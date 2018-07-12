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

package io.github.weresandstorm.springsecurity.builders;

import static io.github.weresandstorm.springsecurity.commons.SecurityRDG.string;

import io.github.weresandstorm.springsecurity.domain.ClientToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.util.SerializationUtils;

public class ClientTokenBuilder {

  private String id = string().next();
  private String tokenId = string().next();
  private byte[] token =
      SerializationUtils.serialize(OAuth2AccessTokenBuilder.oAuth2AccessTokenBuilder().build());
  private String authenticationId = string().next();
  private String username = string().next();
  private String clientId = string().next();

  private ClientTokenBuilder() {}

  public static ClientTokenBuilder kClientTokenBuilder() {
    return new ClientTokenBuilder();
  }

  public ClientToken build() {
    return new ClientToken(id, tokenId, token, username, clientId, authenticationId);
  }

  public ClientTokenBuilder token(final OAuth2AccessToken token) {
    this.token = SerializationUtils.serialize(token);
    return this;
  }
}
