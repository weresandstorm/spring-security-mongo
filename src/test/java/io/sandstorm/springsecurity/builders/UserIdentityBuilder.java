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

package io.sandstorm.springsecurity.builders;

import static io.sandstorm.springsecurity.commons.SecurityRDG.bool;
import static io.sandstorm.springsecurity.commons.SecurityRDG.set;
import static io.sandstorm.springsecurity.commons.SecurityRDG.string;

import io.sandstorm.springsecurity.commons.SecurityRDG;
import io.sandstorm.springsecurity.domain.UserIdentity;
import java.util.Set;
import org.bson.types.ObjectId;
import org.springframework.security.core.GrantedAuthority;

public final class UserIdentityBuilder {

  private String password = string().next();
  private String username = string().next();
  private ObjectId userId = new ObjectId();
  private Set<GrantedAuthority> authorities = set(SecurityRDG.ofGrantedAuthority()).next();
  private boolean accountNonExpired = bool().next();
  private boolean accountNonLocked = bool().next();
  private boolean credentialsNonExpired = bool().next();
  private boolean enabled = bool().next();

  private UserIdentityBuilder() {}

  public static UserIdentityBuilder userIdentityBuilder() {
    return new UserIdentityBuilder();
  }

  public UserIdentity build() {
    return new UserIdentity(
        username,
        password,
        userId,
        authorities,
        accountNonExpired,
        accountNonLocked,
        credentialsNonExpired,
        enabled);
  }

  public UserIdentityBuilder username(final String username) {
    this.username = username;
    return this;
  }

  public UserIdentityBuilder password(final String password) {
    this.password = password;
    return this;
  }

  public UserIdentityBuilder authorities(final Set<GrantedAuthority> authorities) {
    this.authorities = authorities;
    return this;
  }
}
