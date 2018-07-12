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

import java.time.LocalDateTime;
import java.util.Objects;
import org.bson.codecs.pojo.annotations.BsonId;
import org.springframework.security.oauth2.provider.approval.Approval.ApprovalStatus;

public class Approval implements Entity<String> {

  @BsonId public String id;
  public String userName;
  public String clientId;
  public String scope;
  public ApprovalStatus status;
  public LocalDateTime expiresAt;
  public LocalDateTime lastUpdatedAt;

  public Approval() {}

  public Approval(
      final String id,
      final String userName,
      final String clientId,
      final String scope,
      final ApprovalStatus status,
      final LocalDateTime expiresAt,
      final LocalDateTime lastUpdatedAt) {
    this.id = id;
    this.userName = userName;
    this.clientId = clientId;
    this.scope = scope;
    this.status = status;
    this.expiresAt = expiresAt;
    this.lastUpdatedAt = lastUpdatedAt;
  }

  @Override
  public String id() {
    return id;
  }

  public String getUserName() {
    return userName;
  }

  public String getClientId() {
    return clientId;
  }

  public String getScope() {
    return scope;
  }

  public ApprovalStatus getStatus() {
    return status;
  }

  public LocalDateTime getExpiresAt() {
    return expiresAt;
  }

  public LocalDateTime getLastUpdatedAt() {
    return lastUpdatedAt;
  }

  @Override
  public int hashCode() {
    return Objects.hash(userName, clientId, scope, status, expiresAt, lastUpdatedAt);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final Approval other = (Approval) obj;
    return Objects.equals(this.userName, other.userName)
        && Objects.equals(this.clientId, other.clientId)
        && Objects.equals(this.scope, other.scope)
        && Objects.equals(this.status, other.status)
        && Objects.equals(this.expiresAt, other.expiresAt)
        && Objects.equals(this.lastUpdatedAt, other.lastUpdatedAt);
  }

  @Override
  public String toString() {
    return "Approval{"
        + "id='"
        + id
        + '\''
        + ", userName='"
        + userName
        + '\''
        + ", clientId='"
        + clientId
        + '\''
        + ", scope='"
        + scope
        + '\''
        + ", status="
        + status
        + ", expiresAt="
        + expiresAt
        + ", lastUpdatedAt="
        + lastUpdatedAt
        + '}';
  }
}
