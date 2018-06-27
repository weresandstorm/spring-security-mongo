package me.konglong.springsecurity.service;

import static me.konglong.springsecurity.commons.SecurityRDG.string;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import me.konglong.springsecurity.builders.KUserAccountBuilder;
import me.konglong.springsecurity.commons.SecurityRDG;
import me.konglong.springsecurity.domain.KUserAccount;
import me.konglong.springsecurity.domain.UserAccountRepo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

@RunWith(MockitoJUnitRunner.class)
public class UserDetailsManagerTest {

  @Mock private UserAccountRepo userAccountRepo;

  @Mock private AuthenticationManager authenticationManager;

  @Mock private SecurityContextService securityContextService;

  private KUserAccountDetailsManager userDetailsManager;

  @Before
  public void setup() {
    userDetailsManager =
        new KUserAccountDetailsManager(
            userAccountRepo, securityContextService, authenticationManager);
  }

  @Test
  public void shouldCreateUser() {
    // Given
    final KUserAccount userAccount = KUserAccountBuilder.userAccountBuilder().build();

    // When
    userDetailsManager.createUser(userAccount);

    // Then
    verify(userAccountRepo).save(userAccount);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldNotCreateUserWhenUsernameIsEmpty() {
    // Given
    final KUserAccount userAccount = KUserAccountBuilder.userAccountBuilder().username("").build();

    // When
    userDetailsManager.createUser(userAccount);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldNotCreateUserWhenUsernameIsValidAndNoAuthorities() {
    // Given
    final KUserAccount userAccount = KUserAccountBuilder.userAccountBuilder().authorities(null).build();

    // When
    userDetailsManager.createUser(userAccount);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldNotCreateUserWhenUsernameIsValidAndAuthoritiesNotValid() {
    // Given
    final KUserAccount userAccount =
        KUserAccountBuilder.userAccountBuilder()
            .authorities(SecurityRDG.set(SecurityRDG.ofInvalidAuthority()).next())
            .build();

    // When
    userDetailsManager.createUser(userAccount);
  }

  @Test
  public void shouldDeleteUser() {
    // Given
    final String username = string().next();

    // When
    userDetailsManager.deleteUser(username);

    // Then
    verify(userAccountRepo).deleteByUsername(username);
  }

  @Test
  public void shouldUpdateUser() {
    // Given
    final KUserAccount userAccount = KUserAccountBuilder.userAccountBuilder().build();

    // When
    userDetailsManager.updateUser(userAccount);

    // Then
    verify(userAccountRepo).save(userAccount);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldNotUpdateUserWhenUsernameIsInvalid() {
    // Given
    final KUserAccount userAccount = KUserAccountBuilder.userAccountBuilder().username("").build();

    // When
    userDetailsManager.updateUser(userAccount);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldNotUpdateUserWhenUsernameIsValidAndNoAuthorities() {
    // Given
    final KUserAccount userAccount = KUserAccountBuilder.userAccountBuilder().authorities(null).build();

    // When
    userDetailsManager.updateUser(userAccount);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldNotUpdateUserWhenUsernameIsValidAndAuthoritiesNotValid() {
    // Given
    final KUserAccount userAccount =
        KUserAccountBuilder.userAccountBuilder()
            .authorities(SecurityRDG.set(SecurityRDG.ofInvalidAuthority()).next())
            .build();

    // When
    userDetailsManager.updateUser(userAccount);
  }

  @Test
  public void shouldReturnTrueWhenUserExists() {
    // Given
    final String username = string().next();
    final KUserAccount userAccount = KUserAccountBuilder.userAccountBuilder().username(username).build();

    // And
    given(userAccountRepo.findByUsername(username)).willReturn(Optional.of(userAccount));

    // When
    final boolean userExists = userDetailsManager.userExists(username);

    // Then
    assertThat(userExists).isTrue();
  }

  @Test
  public void shouldReturnFalseWhenUserDoesNotExists() {
    // Given
    final String username = string().next();

    // And
    given(userAccountRepo.findByUsername(username)).willReturn(Optional.empty());

    // When
    final boolean userExists = userDetailsManager.userExists(username);

    // Then
    assertThat(userExists).isFalse();
  }

  @Test
  public void shouldLoadUserByUsernameWhenUserExists() {
    // Given
    final String username = string().next();
    System.out.println("userName: " + username);

    // And
    given(userAccountRepo.findByUsername(username))
        .willReturn(Optional.of(KUserAccountBuilder.userAccountBuilder().username(username).build()));

    // And
//    doReturn(Optional.of(KUserAccountBuilder.userAccountBuilder().username(username).build()))
//        .when(userAccountRepo)
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
    final String username = string().next();
    final String currentPassword = string().next();
    final KUserAccount userAccount =
        KUserAccountBuilder.userAccountBuilder().username(username).password(currentPassword).build();

    // And
    final UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(userAccount, currentPassword);
    given(securityContextService.getAuthentication()).willReturn(authenticationToken);

    // And
    given(userAccountRepo.findByUsername(username)).willReturn(Optional.of(userAccount));

    // When
    final String newPassword = string().next();
    userDetailsManager.changePassword(currentPassword, newPassword);

    // Then
    verify(authenticationManager).authenticate(any(Authentication.class));
    verify(securityContextService).setAuthentication(any(Authentication.class));
    verify(userAccountRepo).changePassword(currentPassword, newPassword, username);
  }
}
