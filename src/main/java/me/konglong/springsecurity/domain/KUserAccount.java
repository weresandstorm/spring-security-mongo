package me.konglong.springsecurity.domain;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class KUserAccount implements UserDetails, CredentialsContainer, Entity<String> {

  @BsonId private String username;
  private String password;
  private UUID userUUID;
  private Set<GrantedAuthority> authorities;
  private boolean accountNonExpired;
  private boolean accountNonLocked;
  private boolean credentialsNonExpired;
  private boolean enabled;

  public KUserAccount() {}

  @BsonCreator
  public KUserAccount(
      @BsonProperty("_id") final String username,
      final String password,
      final UUID userUUID,
      final Set<GrantedAuthority> authorities,
      final boolean accountNonExpired,
      final boolean accountNonLocked,
      final boolean credentialsNonExpired,
      final boolean enabled) {
    this.username = username;
    this.password = password;
    this.userUUID = userUUID;
    this.authorities = authorities;
    this.accountNonExpired = accountNonExpired;
    this.accountNonLocked = accountNonLocked;
    this.credentialsNonExpired = credentialsNonExpired;
    this.enabled = enabled;
  }

  @Override
  public String id() {
    return username;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public String getPassword() {
    return password;
  }

  public UUID getUserUUID() {
    return userUUID;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public boolean isAccountNonExpired() {
    return accountNonExpired;
  }

  @Override
  public boolean isAccountNonLocked() {
    return accountNonLocked;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return credentialsNonExpired;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  @Override
  public void eraseCredentials() {
    password = null;
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        password,
        username,
        userUUID,
        authorities,
        accountNonExpired,
        accountNonLocked,
        credentialsNonExpired,
        enabled);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final KUserAccount other = (KUserAccount) obj;
    return Objects.equals(this.password, other.password)
        && Objects.equals(this.username, other.username)
        && Objects.equals(this.userUUID, other.userUUID)
        && Objects.equals(this.authorities, other.authorities)
        && Objects.equals(this.accountNonExpired, other.accountNonExpired)
        && Objects.equals(this.accountNonLocked, other.accountNonLocked)
        && Objects.equals(this.credentialsNonExpired, other.credentialsNonExpired)
        && Objects.equals(this.enabled, other.enabled);
  }

  @Override
  public String toString() {
    return "KUserAccount{"
        + "username='"
        + username
        + '\''
        + ", password='"
        + password
        + '\''
        + ", userUUID="
        + userUUID
        + ", authorities="
        + authorities
        + ", accountNonExpired="
        + accountNonExpired
        + ", accountNonLocked="
        + accountNonLocked
        + ", credentialsNonExpired="
        + credentialsNonExpired
        + ", enabled="
        + enabled
        + '}';
  }
}
