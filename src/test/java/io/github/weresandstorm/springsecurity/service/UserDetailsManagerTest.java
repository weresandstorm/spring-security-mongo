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

import io.github.weresandstorm.springsecurity.builders.UserIdentityBuilder;
import io.github.weresandstorm.springsecurity.commons.SecurityRDG;
import io.github.weresandstorm.springsecurity.domain.UserIdentity;
import io.github.weresandstorm.springsecurity.domain.UserIdentityRepo;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import uk.org.fyodor.generators.RDG;

@RunWith(MockitoJUnitRunner.class)
public class UserDetailsManagerTest {

  @Mock private UserIdentityRepo userIdentityRepo;

  @Mock private AuthenticationManager authenticationManager;

  @Mock private SecurityContextService securityContextService;

  private ConcreteUserDetailsManager userDetailsManager;

  @Before
  public void setup() {
    userDetailsManager =
        new ConcreteUserDetailsManager(
            userIdentityRepo, securityContextService, authenticationManager);
  }

  @Test
  public void shouldCreateUser() {
    // Given
    final UserIdentity userAccount = UserIdentityBuilder.userIdentityBuilder().build();

    // When
    userDetailsManager.createUser(userAccount);

    // Then
    verify(userIdentityRepo).save(userAccount);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldNotCreateUserWhenUsernameIsEmpty() {
    // Given
    final UserIdentity userAccount = UserIdentityBuilder.userIdentityBuilder().username("").build();

    // When
    userDetailsManager.createUser(userAccount);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldNotCreateUserWhenUsernameIsValidAndNoAuthorities() {
    // Given
    final UserIdentity userAccount =
        UserIdentityBuilder.userIdentityBuilder().authorities(null).build();

    // When
    userDetailsManager.createUser(userAccount);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldNotCreateUserWhenUsernameIsValidAndAuthoritiesNotValid() {
    // Given
    final UserIdentity userAccount =
        UserIdentityBuilder.userIdentityBuilder()
            .authorities(SecurityRDG.set(SecurityRDG.ofInvalidAuthority()).next())
            .build();

    // When
    userDetailsManager.createUser(userAccount);
  }

  @Test
  public void shouldDeleteUser() {
    // Given
    final String username = RDG.string().next();

    // When
    userDetailsManager.deleteUser(username);

    // Then
    verify(userIdentityRepo).deleteByUsername(username);
  }

  @Test
  public void shouldUpdateUser() {
    // Given
    final UserIdentity userAccount = UserIdentityBuilder.userIdentityBuilder().build();

    // When
    userDetailsManager.updateUser(userAccount);

    // Then
    verify(userIdentityRepo).save(userAccount);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldNotUpdateUserWhenUsernameIsInvalid() {
    // Given
    final UserIdentity userAccount = UserIdentityBuilder.userIdentityBuilder().username("").build();

    // When
    userDetailsManager.updateUser(userAccount);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldNotUpdateUserWhenUsernameIsValidAndNoAuthorities() {
    // Given
    final UserIdentity userAccount =
        UserIdentityBuilder.userIdentityBuilder().authorities(null).build();

    // When
    userDetailsManager.updateUser(userAccount);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldNotUpdateUserWhenUsernameIsValidAndAuthoritiesNotValid() {
    // Given
    final UserIdentity userAccount =
        UserIdentityBuilder.userIdentityBuilder()
            .authorities(SecurityRDG.set(SecurityRDG.ofInvalidAuthority()).next())
            .build();

    // When
    userDetailsManager.updateUser(userAccount);
  }

  @Test
  public void shouldReturnTrueWhenUserExists() {
    // Given
    final String username = RDG.string().next();
    final UserIdentity userAccount =
        UserIdentityBuilder.userIdentityBuilder().username(username).build();

    // And
    given(userIdentityRepo.findByUsername(username)).willReturn(Optional.of(userAccount));

    // When
    final boolean userExists = userDetailsManager.userExists(username);

    // Then
    assertThat(userExists).isTrue();
  }

  @Test
  public void shouldReturnFalseWhenUserDoesNotExists() {
    // Given
    final String username = RDG.string().next();

    // And
    given(userIdentityRepo.findByUsername(username)).willReturn(Optional.empty());

    // When
    final boolean userExists = userDetailsManager.userExists(username);

    // Then
    assertThat(userExists).isFalse();
  }

  @Test
  public void shouldLoadUserByUsernameWhenUserExists() {
    // Given
    final String username = RDG.string().next();
    System.out.println("userName: " + username);

    // And
    given(userIdentityRepo.findByUsername(username))
        .willReturn(
            Optional.of(UserIdentityBuilder.userIdentityBuilder().username(username).build()));

    // And
    //
    // doReturn(Optional.of(UserIdentityBuilder.userIdentityBuilder().username(username).build()))
    //        .when(userIdentityRepo)
    //        .findByUsername(username);

    // When
    final UserDetails user = userDetailsManager.loadUserByUsername(username);

    // Then
    assertThat(user).isNotNull();
    assertThat(user.getUsername()).isEqualTo(username);
  }

  @Test
  public void shouldChangePasswordForExitingUser() {
    // Given
    final String username = RDG.string().next();
    final String currentPassword = RDG.string().next();
    final UserIdentity userAccount =
        UserIdentityBuilder.userIdentityBuilder()
            .username(username)
            .password(currentPassword)
            .build();

    // And
    final UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(userAccount, currentPassword);
    given(securityContextService.getAuthentication()).willReturn(authenticationToken);

    // And
    given(userIdentityRepo.findByUsername(username)).willReturn(Optional.of(userAccount));

    // When
    final String newPassword = RDG.string().next();
    userDetailsManager.changePassword(currentPassword, newPassword);

    // Then
    verify(authenticationManager).authenticate(any(Authentication.class));
    verify(securityContextService).setAuthentication(any(Authentication.class));
    verify(userIdentityRepo).changePassword(currentPassword, newPassword, username);
  }
}
