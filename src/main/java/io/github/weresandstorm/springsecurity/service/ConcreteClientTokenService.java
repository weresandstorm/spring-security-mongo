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

import io.github.weresandstorm.springsecurity.domain.ClientToken;
import io.github.weresandstorm.springsecurity.domain.ClientTokenRepo;
import java.util.UUID;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.ClientKeyGenerator;
import org.springframework.security.oauth2.client.token.ClientTokenServices;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import org.springframework.stereotype.Component;

@Component
public class ConcreteClientTokenService implements ClientTokenServices {

  private final ClientTokenRepo clientTokenRepo;

  private final ClientKeyGenerator clientKeyGenerator;

  public ConcreteClientTokenService(
      final ClientTokenRepo clientTokenRepo, final ClientKeyGenerator clientKeyGenerator) {
    this.clientTokenRepo = clientTokenRepo;
    this.clientKeyGenerator = clientKeyGenerator;
  }

  @Override
  public OAuth2AccessToken getAccessToken(
      final OAuth2ProtectedResourceDetails resource, final Authentication authentication) {
    final ClientToken ClientToken =
        clientTokenRepo.findByAuthenticationId(
            clientKeyGenerator.extractKey(resource, authentication));
    return SerializationUtils.deserialize(ClientToken.getToken());
  }

  @Override
  public void saveAccessToken(
      final OAuth2ProtectedResourceDetails resource,
      final Authentication authentication,
      final OAuth2AccessToken accessToken) {
    removeAccessToken(resource, authentication);
    final ClientToken ClientToken =
        new ClientToken(
            UUID.randomUUID().toString(),
            accessToken.getValue(),
            SerializationUtils.serialize(accessToken),
            authentication.getName(),
            resource.getClientId(),
            clientKeyGenerator.extractKey(resource, authentication));

    clientTokenRepo.save(ClientToken);
  }

  @Override
  public void removeAccessToken(
      final OAuth2ProtectedResourceDetails resource, final Authentication authentication) {
    clientTokenRepo.deleteByAuthenticationId(
        clientKeyGenerator.extractKey(resource, authentication));
  }
}
