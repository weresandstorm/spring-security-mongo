package me.konglong.springsecurity.builders;

import static me.konglong.springsecurity.commons.SecurityRDG.bool;
import static me.konglong.springsecurity.commons.SecurityRDG.set;
import static me.konglong.springsecurity.commons.SecurityRDG.string;

import java.util.Set;
import java.util.UUID;
import me.konglong.springsecurity.commons.SecurityRDG;
import me.konglong.springsecurity.domain.KUserAccount;
import org.springframework.security.core.GrantedAuthority;

public final class KUserAccountBuilder {

  private String password = string().next();
  private String username = string().next();
  private UUID userUUID = UUID.randomUUID();
  private Set<GrantedAuthority> authorities = set(SecurityRDG.ofGrantedAuthority()).next();
  private boolean accountNonExpired = bool().next();
  private boolean accountNonLocked = bool().next();
  private boolean credentialsNonExpired = bool().next();
  private boolean enabled = bool().next();

  private KUserAccountBuilder() {}

  public static KUserAccountBuilder userAccountBuilder() {
    return new KUserAccountBuilder();
  }

  public KUserAccount build() {
    return new KUserAccount(
        username,
        password,
        userUUID,
        authorities,
        accountNonExpired,
        accountNonLocked,
        credentialsNonExpired,
        enabled);
  }

  public KUserAccountBuilder username(final String username) {
    this.username = username;
    return this;
  }

  public KUserAccountBuilder password(final String password) {
    this.password = password;
    return this;
  }

  public KUserAccountBuilder authorities(final Set<GrantedAuthority> authorities) {
    this.authorities = authorities;
    return this;
  }
}
