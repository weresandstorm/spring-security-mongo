package me.konglong.springsecurity.builders;

import static me.konglong.springsecurity.commons.SecurityRDG.localDateTime;
import static uk.org.fyodor.generators.RDG.string;
import static uk.org.fyodor.generators.RDG.value;

import java.time.LocalDateTime;
import java.util.UUID;
import me.konglong.springsecurity.domain.KApproval;
import org.springframework.security.oauth2.provider.approval.Approval.ApprovalStatus;

public class KApprovalBuilder {

  private String id = UUID.randomUUID().toString();
  private String userId = string().next();
  private String clientId = string().next();
  private String scope = string().next();
  private ApprovalStatus status = value(ApprovalStatus.class).next();
  private LocalDateTime expiresAt = localDateTime().next();
  private LocalDateTime lastUpdatedAt = localDateTime().next();

  private KApprovalBuilder() {}

  public static KApprovalBuilder kApprovalBuilder() {
    return new KApprovalBuilder();
  }

  public KApproval build() {
    return new KApproval(id, userId, clientId, scope, status, expiresAt, lastUpdatedAt);
  }

  public KApprovalBuilder clientId(String clientId) {
    this.clientId = clientId;
    return this;
  }

  public KApprovalBuilder userId(String userId) {
    this.userId = userId;
    return this;
  }
}
