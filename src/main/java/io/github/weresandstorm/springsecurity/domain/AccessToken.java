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

import java.util.Arrays;
import java.util.Objects;
import org.bson.codecs.pojo.annotations.BsonId;

public class AccessToken implements Entity<String> {

  @BsonId public String id;
  public byte[] token;
  public String refreshTokenId;
  public String username;
  public String clientId;
  public String authenticationId;
  public byte[] authentication;

  public AccessToken() {}

  public AccessToken(
      final String id,
      final byte[] token,
      final String authenticationId,
      final String username,
      final String clientId,
      final byte[] authentication,
      final String refreshTokenId) {
    this.id = id;
    this.token = token;
    this.authenticationId = authenticationId;
    this.username = username;
    this.clientId = clientId;
    this.authentication = authentication;
    this.refreshTokenId = refreshTokenId;
  }

  @Override
  public String id() {
    return id;
  }

  public byte[] getToken() {
    return token;
  }

  public String getRefreshTokenId() {
    return refreshTokenId;
  }

  public String getUsername() {
    return username;
  }

  public String getClientId() {
    return clientId;
  }

  public String getAuthenticationId() {
    return authenticationId;
  }

  public byte[] getAuthentication() {
    return authentication;
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        token, authenticationId, username, clientId, authentication, refreshTokenId);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final AccessToken other = (AccessToken) obj;
    return Objects.equals(this.token, other.token)
        && Objects.equals(this.authenticationId, other.authenticationId)
        && Objects.equals(this.username, other.username)
        && Objects.equals(this.clientId, other.clientId)
        && Objects.equals(this.authentication, other.authentication)
        && Objects.equals(this.refreshTokenId, other.refreshTokenId);
  }

  @Override
  public String toString() {
    return "AccessToken{"
        + "id='"
        + id
        + "'"
        + ", token="
        + Arrays.toString(token)
        + ", authenticationId='"
        + authenticationId
        + "'"
        + ", username='"
        + username
        + "'"
        + ", clientId='"
        + clientId
        + "'"
        + ", authentication="
        + Arrays.toString(authentication)
        + ", refreshTokenId='"
        + refreshTokenId
        + "'"
        + "}";
  }
}
