package me.konglong.springsecurity.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import org.springframework.security.oauth2.provider.approval.Approval.ApprovalStatus;

public class KApproval implements Entity<String> {

  private String id;
  private String userName;
  private String clientId;
  private String scope;
  private ApprovalStatus status;
  private LocalDateTime expiresAt;
  private LocalDateTime lastUpdatedAt;

  public KApproval() {}

  public KApproval(
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
    final KApproval other = (KApproval) obj;
    return Objects.equals(this.userName, other.userName)
        && Objects.equals(this.clientId, other.clientId)
        && Objects.equals(this.scope, other.scope)
        && Objects.equals(this.status, other.status)
        && Objects.equals(this.expiresAt, other.expiresAt)
        && Objects.equals(this.lastUpdatedAt, other.lastUpdatedAt);
  }

  @Override
  public String toString() {
    return "KApproval{"
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
