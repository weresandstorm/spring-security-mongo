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

import io.github.weresandstorm.springsecurity.builders.ClientDetailsBuilder;
import io.github.weresandstorm.springsecurity.config.ApplicationConf;
import io.github.weresandstorm.springsecurity.domain.ClientDetailsRepo;
import io.github.weresandstorm.springsecurity.domain.ConcreteClientDetails;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = {ApplicationConf.class})
@TestPropertySource("classpath:mongo.properties")
@ActiveProfiles("test")
@DirtiesContext
public class ClientDetailsServiceIntegTest {

  @Autowired private ConcreteClientDetailsService clientDetailsService;

  @Autowired private ClientDetailsRepo clientDetailsRepo;

  @Autowired private PasswordEncoder passwordEncoder;

  @Test
  public void shouldPersistClientDetailsSuccessfully() {
    // Given
    final ConcreteClientDetails clientDetails =
        ClientDetailsBuilder.concreteClientDetailsBuilder().build();

    // When
    clientDetailsService.addClientDetails(clientDetails);

    // Then
    clientDetails.clientSecret = passwordEncoder.encode(clientDetails.clientSecret);
    final ConcreteClientDetails expectedClientDetails =
        clientDetailsRepo.findByClientId(clientDetails.getClientId());
    assertThat(expectedClientDetails).isNotNull();
    assertThat(expectedClientDetails).isEqualTo(clientDetails);
  }

  @Test
  public void shouldLoadClientDetailsByIdSuccessfully() {
    // Given
    final ConcreteClientDetails clientDetails =
        ClientDetailsBuilder.concreteClientDetailsBuilder().build();

    // And
    clientDetailsService.addClientDetails(clientDetails);

    // When
    final ClientDetails expectedClientDetails =
        clientDetailsService.loadClientByClientId(clientDetails.getClientId());

    // Then
    assertThat(expectedClientDetails).isNotNull();
    assertThat(expectedClientDetails).isEqualTo(clientDetails);
  }

  @Test
  public void shouldGetListOfClientDetailsByIdSuccessfully() {
    // Given
    final ConcreteClientDetails clientDetails =
        ClientDetailsBuilder.concreteClientDetailsBuilder().build();

    // And
    clientDetailsService.addClientDetails(clientDetails);

    // When
    final List<ClientDetails> expectedClientDetails = clientDetailsService.listClientDetails();

    // Then
    assertThat(expectedClientDetails).contains(clientDetails);
  }
}
