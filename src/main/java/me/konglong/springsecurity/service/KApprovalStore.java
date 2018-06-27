package me.konglong.springsecurity.service;

import static java.util.Objects.isNull;
import static me.konglong.springsecurity.util.LocalDateTimeUtil.convertTolocalDateTimeFrom;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import me.konglong.springsecurity.domain.ApprovalRepo;
import me.konglong.springsecurity.domain.KApproval;
import org.springframework.security.oauth2.provider.approval.Approval;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.stereotype.Component;

@Component
public class KApprovalStore implements ApprovalStore {

  private final ApprovalRepo approvalRepo;

  private boolean handleRevocationsAsExpiry = false;

  public KApprovalStore(final ApprovalRepo approvalRepo) {
    this.approvalRepo = approvalRepo;
  }

  @Override
  public boolean addApprovals(final Collection<Approval> approvals) {
    final Collection<KApproval> kApprovals = transformToKApproval(approvals);
    return approvalRepo.updateOrCreate(kApprovals);
  }

  @Override
  public boolean revokeApprovals(final Collection<Approval> approvals) {
    boolean success = true;

    final Collection<KApproval> mongoApprovals = transformToKApproval(approvals);

    for (final KApproval approval : mongoApprovals) {
      if (handleRevocationsAsExpiry) {
        final boolean updateResult = approvalRepo.updateExpiresAt(LocalDateTime.now(), approval);
        if (!updateResult) {
          success = false;
        }

      } else {
        final boolean deleteResult = approvalRepo.deleteByUserIdAndClientIdAndScope(approval);

        if (!deleteResult) {
          success = false;
        }
      }
    }
    return success;
  }

  @Override
  public Collection<Approval> getApprovals(final String userId, final String clientId) {
    final List<KApproval> approvals = approvalRepo.findByUserIdAndClientId(userId, clientId);
    return transformToApprovals(approvals);
  }

  private List<Approval> transformToApprovals(final List<KApproval> approvals) {
    return approvals
        .stream()
        .map(
            kApproval ->
                new Approval(
                    kApproval.getUserName(),
                    kApproval.getClientId(),
                    kApproval.getScope(),
                    Date.from(
                        kApproval.getExpiresAt().atZone(ZoneId.systemDefault()).toInstant()),
                    kApproval.getStatus(),
                    Date.from(
                        kApproval
                            .getLastUpdatedAt()
                            .atZone(ZoneId.systemDefault())
                            .toInstant())))
        .collect(Collectors.toList());
  }

  private List<KApproval> transformToKApproval(final Collection<Approval> approvals) {
    return approvals
        .stream()
        .map(
            approval ->
                new KApproval(
                    UUID.randomUUID().toString(),
                    approval.getUserId(),
                    approval.getClientId(),
                    approval.getScope(),
                    isNull(approval.getStatus())
                        ? Approval.ApprovalStatus.APPROVED
                        : approval.getStatus(),
                    convertTolocalDateTimeFrom(approval.getExpiresAt()),
                    convertTolocalDateTimeFrom(approval.getLastUpdatedAt())))
        .collect(Collectors.toList());
  }

  public void setHandleRevocationsAsExpiry(boolean handleRevocationsAsExpiry) {
    this.handleRevocationsAsExpiry = handleRevocationsAsExpiry;
  }
}
