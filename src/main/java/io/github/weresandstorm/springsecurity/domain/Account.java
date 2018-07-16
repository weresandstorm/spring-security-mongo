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

package io.github.weresandstorm.springsecurity.domain;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.types.ObjectId;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class Account implements UserDetails, CredentialsContainer, Entity<String> {

  @BsonId public String username;
  public String password;
  public ObjectId userId;
  public Set<GrantedAuthority> grantedAuthorities;
  public boolean nonExpired;
  public boolean nonLocked;
  public boolean credentialsNonExpired;
  public boolean enabled;

  public Account() {}

  public Account(
      final String username,
      final String password,
      final ObjectId userId,
      final Set<GrantedAuthority> grantedAuthorities,
      final boolean nonExpired,
      final boolean nonLocked,
      final boolean credentialsNonExpired,
      final boolean enabled) {
    this.username = username;
    this.password = password;
    this.userId = userId;
    this.grantedAuthorities = grantedAuthorities;
    this.nonExpired = nonExpired;
    this.nonLocked = nonLocked;
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

  public ObjectId getUserId() {
    return userId;
  }

  @BsonIgnore
  @Override
  public Collection<GrantedAuthority> getAuthorities() {
    return grantedAuthorities;
  }

  @BsonIgnore
  @Override
  public boolean isAccountNonExpired() {
    return nonExpired;
  }

  @BsonIgnore
  @Override
  public boolean isAccountNonLocked() {
    return nonLocked;
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
        userId,
        grantedAuthorities,
        nonExpired,
        nonLocked,
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
    final Account other = (Account) obj;
    return Objects.equals(this.password, other.password)
        && Objects.equals(this.username, other.username)
        && Objects.equals(this.userId, other.userId)
        && Objects.equals(this.grantedAuthorities, other.grantedAuthorities)
        && Objects.equals(this.nonExpired, other.nonExpired)
        && Objects.equals(this.nonLocked, other.nonLocked)
        && Objects.equals(this.credentialsNonExpired, other.credentialsNonExpired)
        && Objects.equals(this.enabled, other.enabled);
  }

  @Override
  public String toString() {
    return "Account{"
        + "username='"
        + username
        + '\''
        + ", password='"
        + password
        + '\''
        + ", userId="
        + userId
        + ", grantedAuthorities="
        + grantedAuthorities
        + ", nonExpired="
        + nonExpired
        + ", nonLocked="
        + nonLocked
        + ", credentialsNonExpired="
        + credentialsNonExpired
        + ", enabled="
        + enabled
        + '}';
  }
}
