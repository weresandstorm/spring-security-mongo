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

public class ClientToken implements Entity<String> {

  @BsonId public String id;
  public String tokenId;
  public byte[] token;
  public String username;
  public String clientId;
  public String authenticationId;

  public ClientToken() {}

  public ClientToken(
      final String id,
      final String tokenId,
      final byte[] token,
      final String username,
      final String clientId,
      final String authenticationId) {
    this.id = id;
    this.tokenId = tokenId;
    this.token = token;
    this.username = username;
    this.clientId = clientId;
    this.authenticationId = authenticationId;
  }

  @Override
  public String id() {
    return id;
  }

  public String getTokenId() {
    return tokenId;
  }

  public byte[] getToken() {
    return token;
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

  @Override
  public int hashCode() {
    return Objects.hash(tokenId, token, username, clientId, authenticationId);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final ClientToken other = (ClientToken) obj;
    return Objects.equals(this.tokenId, other.tokenId)
        && Objects.equals(this.token, other.token)
        && Objects.equals(this.authenticationId, other.authenticationId)
        && Objects.equals(this.username, other.username)
        && Objects.equals(this.clientId, other.clientId);
  }

  @Override
  public String toString() {
    return "ClientToken{"
        + "id='"
        + id
        + '\''
        + ", tokenId='"
        + tokenId
        + '\''
        + ", token="
        + Arrays.toString(token)
        + ", authenticationId='"
        + authenticationId
        + '\''
        + ", username='"
        + username
        + '\''
        + ", clientId='"
        + clientId
        + '\''
        + '}';
  }
}
