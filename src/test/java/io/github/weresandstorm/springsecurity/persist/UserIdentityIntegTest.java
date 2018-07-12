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

package io.github.weresandstorm.springsecurity.persist;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.org.fyodor.generators.RDG.string;

import io.github.weresandstorm.springsecurity.builders.UserIdentityBuilder;
import io.github.weresandstorm.springsecurity.config.PersistConf;
import io.github.weresandstorm.springsecurity.domain.UserIdentity;
import io.github.weresandstorm.springsecurity.domain.UserIdentityRepo;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PersistConf.class)
@TestPropertySource("classpath:mongo.properties")
public class UserIdentityIntegTest {

  @Autowired private UserIdentityRepo userIdentityRepo;

  @Test
  public void shouldSaveUser() {
    // Given
    UserIdentity account = UserIdentityBuilder.userIdentityBuilder().build();

    // When
    UserIdentity result = userIdentityRepo.save(account);

    // Then
    Optional<UserIdentity> expectedAccount = userIdentityRepo.findByUsername(account.getUsername());
    assertThat(result).isEqualTo(expectedAccount.get());
  }

  @Test
  public void shouldChangePasswordUser() {
    // Given
    UserIdentity account = UserIdentityBuilder.userIdentityBuilder().build();
    userIdentityRepo.save(account);

    // When
    final boolean result =
        userIdentityRepo.changePassword(
            account.getPassword(), string().next(), account.getUsername());

    // Then
    assertThat(result).isTrue();
  }
}
