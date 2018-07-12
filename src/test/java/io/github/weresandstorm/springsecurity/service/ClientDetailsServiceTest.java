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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import io.github.weresandstorm.springsecurity.builders.ClientDetailsBuilder;
import io.github.weresandstorm.springsecurity.builders.Oauth2ClientDetailsBuilder;
import io.github.weresandstorm.springsecurity.domain.ClientDetailsRepo;
import io.github.weresandstorm.springsecurity.domain.ConcreteClientDetails;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import uk.org.fyodor.generators.RDG;

@RunWith(MockitoJUnitRunner.class)
public class ClientDetailsServiceTest {

  @Mock private ClientDetailsRepo clientDetailsRepo;

  @Mock private PasswordEncoder passwordEncoder;

  private ConcreteClientDetailsService clientDetailsService;

  @Before
  public void setup() {
    clientDetailsService = new ConcreteClientDetailsService(clientDetailsRepo, passwordEncoder);
  }

  @Test
  public void shouldAddClientDetails() {
    // Given
    final ClientDetails clientDetails = Oauth2ClientDetailsBuilder.clientDetailsBuilder().build();

    // When
    clientDetailsService.addClientDetails(clientDetails);

    // Then
    verify(clientDetailsRepo).save(any(ConcreteClientDetails.class));
    verify(passwordEncoder).encode(clientDetails.getClientSecret());
  }

  @Test
  public void shouldRemoveClientDetailsWithValidClientId() {
    // Given
    final String clientId = RDG.string().next();

    // And
    given(clientDetailsRepo.deleteByClientId(clientId)).willReturn(true);

    // When
    clientDetailsService.removeClientDetails(clientId);
  }

  @Test(expected = NoSuchClientException.class)
  public void shouldThrowsExceptionWhenTryToRemoveClientDetailsWithInvalidClientId() {
    // Given
    final String clientId = RDG.string().next();

    // And
    given(clientDetailsRepo.deleteByClientId(clientId)).willReturn(false);

    // When
    clientDetailsService.removeClientDetails(clientId);
  }

  @Test
  public void shouldUpdateClientDetails() throws NoSuchClientException {
    // Given
    final ClientDetails clientDetails = Oauth2ClientDetailsBuilder.clientDetailsBuilder().build();

    // And
    given(clientDetailsRepo.update(any(ConcreteClientDetails.class))).willReturn(true);

    // When
    clientDetailsService.updateClientDetails(clientDetails);
  }

  @Test(expected = NoSuchClientException.class)
  public void shouldNotUpdateClientDetailsWhenClientIdIsNotValid() throws NoSuchClientException {
    // Given
    final ClientDetails clientDetails = Oauth2ClientDetailsBuilder.clientDetailsBuilder().build();

    // And
    given(clientDetailsRepo.update(any(ConcreteClientDetails.class))).willReturn(false);

    // When
    clientDetailsService.updateClientDetails(clientDetails);
  }

  @Test
  public void shouldUpdateClientSecret() throws NoSuchClientException {
    // Given
    final String clientId = RDG.string().next();
    final String secret = RDG.string().next();

    // And
    final String expectedNewSecret = RDG.string().next();
    given(passwordEncoder.encode(secret)).willReturn(expectedNewSecret);

    // And
    given(clientDetailsRepo.updateClientSecret(clientId, expectedNewSecret)).willReturn(true);

    // When
    clientDetailsService.updateClientSecret(clientId, secret);
  }

  @Test(expected = NoSuchClientException.class)
  public void shouldNotUpdateClientSecretWhenClientIdIsInvalid() throws NoSuchClientException {
    // Given
    final String clientId = RDG.string().next();
    final String secret = RDG.string().next();

    // And
    final String expectedNewSecret = RDG.string().next();
    given(passwordEncoder.encode(secret)).willReturn(expectedNewSecret);

    // And
    given(clientDetailsRepo.updateClientSecret(clientId, expectedNewSecret)).willReturn(false);

    // When
    clientDetailsService.updateClientSecret(clientId, secret);
  }

  @Test
  public void shouldLoadClientByClientId() throws NoSuchClientException {
    // Given
    final String clientId = RDG.string().next();

    final ConcreteClientDetails expectedClientDetails =
        ClientDetailsBuilder.concreteClientDetailsBuilder().build();
    given(clientDetailsRepo.findByClientId(clientId)).willReturn(expectedClientDetails);

    // When
    final ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);

    // Then
    assertThat(clientDetails.getClientId()).isEqualTo(expectedClientDetails.getClientId());
  }
}
