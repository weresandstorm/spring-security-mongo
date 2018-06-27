package me.konglong.springsecurity.service;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Optional;
import me.konglong.springsecurity.domain.KUserAccount;
import me.konglong.springsecurity.domain.UserAccountRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class KUserAccountDetailsManager implements UserDetailsManager {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  private final UserAccountRepo userAccountRepo;

  private AuthenticationManager authenticationManager;

  private SecurityContextService securityContextService;

  public KUserAccountDetailsManager(
      final UserAccountRepo userRepository,
      final SecurityContextService securityContextService,
      final AuthenticationManager authenticationManager) {
    this.userAccountRepo = userRepository;
    this.securityContextService = securityContextService;
    this.authenticationManager = authenticationManager;
  }

  @Override
  public void createUser(final UserDetails user) {
    validateUserDetails(user);
    userAccountRepo.save(getUser(user));
  }

  private KUserAccount getUser(UserDetails userDetails) {
    final KUserAccount userAccount = (KUserAccount) userDetails;
    return new KUserAccount(
        userAccount.getUsername(),
        userAccount.getPassword(),
        userAccount.getUserUUID(),
        Sets.newConcurrentHashSet(userAccount.getAuthorities()),
        userAccount.isAccountNonExpired(),
        userAccount.isAccountNonLocked(),
        userAccount.isCredentialsNonExpired(),
        userAccount.isEnabled());
  }

  @Override
  public void updateUser(final UserDetails user) {
    validateUserDetails(user);
    userAccountRepo.save(getUser(user));
  }

  @Override
  public void deleteUser(final String username) {
    userAccountRepo.deleteByUsername(username);
  }

  @Override
  public void changePassword(final String oldPassword, final String newPassword) {
    final Authentication currentUser = securityContextService.getAuthentication();

    if (isNull(currentUser)) {
      // This would indicate bad coding somewhere
      throw new AccessDeniedException(
          "Can't change password as no Authentication object found in context "
              + "for current user.");
    }

    final String username = currentUser.getName();

    // If an authentication manager has been set, re-authenticate the user with the supplied
    // password.
    if (nonNull(authenticationManager)) {
      logger.debug("Reauthenticating user '" + username + "' for password change request.");

      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(username, oldPassword));
    } else {
      logger.debug("No authentication manager set. Password won't be re-checked.");
    }

    logger.debug("Changing password for user '" + username + "'");

    userAccountRepo.changePassword(oldPassword, newPassword, username);

    securityContextService.setAuthentication(createNewAuthentication(currentUser));
  }

  @Override
  public boolean userExists(final String username) {
    return userAccountRepo.findByUsername(username).isPresent();
  }

  @Override
  public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
    final Optional<KUserAccount> byUsername = userAccountRepo.findByUsername(username);
    if (byUsername.isPresent()) {
      return byUsername.get();
    }
    throw new UsernameNotFoundException("user does not exists.");
  }

  protected Authentication createNewAuthentication(final Authentication currentAuth) {
    final UserDetails user = loadUserByUsername(currentAuth.getName());

    final UsernamePasswordAuthenticationToken newAuthentication =
        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    newAuthentication.setDetails(currentAuth.getDetails());

    return newAuthentication;
  }

  private void validateUserDetails(UserDetails user) {
    Assert.hasText(user.getUsername(), "Username may not be empty or null");
    validateAuthorities(user.getAuthorities());
  }

  private void validateAuthorities(Collection<? extends GrantedAuthority> authorities) {
    Assert.notNull(authorities, "Authorities list must not be null");

    for (GrantedAuthority authority : authorities) {
      Assert.notNull(authority, "Authorities list contains a null entry");
      Assert.hasText(
          authority.getAuthority(), "getAuthority() method must return a non-empty string");
    }
  }
}
