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

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

import io.sandstorm.springsecurity.domain.ClientDetailsRepo;
import io.sandstorm.springsecurity.domain.ConcreteClientDetails;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.ClientRegistrationService;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.stereotype.Component;

@Component
public class ConcreteClientDetailsService
    implements ClientDetailsService, ClientRegistrationService {

  private final ClientDetailsRepo clientDetailsRepo;

  private final PasswordEncoder passwordEncoder;

  public ConcreteClientDetailsService(
      final ClientDetailsRepo clientDetailsRepo, final PasswordEncoder passwordEncoder) {
    this.clientDetailsRepo = clientDetailsRepo;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public ClientDetails loadClientByClientId(final String clientId) {
    try {
      return clientDetailsRepo.findByClientId(clientId);
    } catch (IllegalArgumentException e) {
      throw new ClientRegistrationException("No Client Details for client id", e);
    }
  }

  @Override
  public void addClientDetails(final ClientDetails clientDetails) {
    final ConcreteClientDetails concreteClientDetails =
        new ConcreteClientDetails(
            clientDetails.getClientId(),
            passwordEncoder.encode(clientDetails.getClientSecret()),
            clientDetails.getScope(),
            clientDetails.getResourceIds(),
            clientDetails.getAuthorizedGrantTypes(),
            clientDetails.getRegisteredRedirectUri(),
            newArrayList(clientDetails.getAuthorities()),
            clientDetails.getAccessTokenValiditySeconds(),
            clientDetails.getRefreshTokenValiditySeconds(),
            clientDetails.getAdditionalInformation(),
            getAutoApproveScopes(clientDetails));

    clientDetailsRepo.save(concreteClientDetails);
  }

  @Override
  public void updateClientDetails(final ClientDetails clientDetails) {
    final ConcreteClientDetails concreteClientDetails =
        new ConcreteClientDetails(
            clientDetails.getClientId(),
            clientDetails.getClientSecret(),
            clientDetails.getScope(),
            clientDetails.getResourceIds(),
            clientDetails.getAuthorizedGrantTypes(),
            clientDetails.getRegisteredRedirectUri(),
            newArrayList(clientDetails.getAuthorities()),
            clientDetails.getAccessTokenValiditySeconds(),
            clientDetails.getRefreshTokenValiditySeconds(),
            clientDetails.getAdditionalInformation(),
            getAutoApproveScopes(clientDetails));
    final boolean result = clientDetailsRepo.update(concreteClientDetails);

    if (!result) {
      throw new NoSuchClientException("No such Client Id");
    }
  }

  @Override
  public void updateClientSecret(final String clientId, final String secret) {
    final boolean result =
        clientDetailsRepo.updateClientSecret(clientId, passwordEncoder.encode(secret));
    if (!result) {
      throw new NoSuchClientException("No such client id");
    }
  }

  @Override
  public void removeClientDetails(String clientId) {
    final boolean result = clientDetailsRepo.deleteByClientId(clientId);
    if (!result) {
      throw new NoSuchClientException("No such client id");
    }
  }

  @Override
  public List<ClientDetails> listClientDetails() {
    final List<ConcreteClientDetails> allClientDetails = clientDetailsRepo.findAll();
    return newArrayList(allClientDetails);
  }

  private Set<String> getAutoApproveScopes(final ClientDetails clientDetails) {
    if (clientDetails.isAutoApprove("true")) {
      return newHashSet("true"); // all scopes autoapproved
    }

    return clientDetails
        .getScope()
        .stream()
        .filter(clientDetails::isAutoApprove)
        .collect(Collectors.toSet());
  }
}
