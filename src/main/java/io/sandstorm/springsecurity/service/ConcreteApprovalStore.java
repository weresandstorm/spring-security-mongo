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

package io.sandstorm.springsecurity.service;

import static java.util.Objects.isNull;

import io.sandstorm.springsecurity.domain.Approval;
import io.sandstorm.springsecurity.domain.ApprovalRepo;
import io.sandstorm.springsecurity.util.LocalDateTimeUtil;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.stereotype.Component;

@Component
public class ConcreteApprovalStore implements ApprovalStore {

  private final ApprovalRepo approvalRepo;

  private boolean handleRevocationsAsExpiry = false;

  public ConcreteApprovalStore(final ApprovalRepo approvalRepo) {
    this.approvalRepo = approvalRepo;
  }

  @Override
  public boolean addApprovals(
      final Collection<org.springframework.security.oauth2.provider.approval.Approval> approvals) {
    final Collection<Approval> kApprovals = transformToKApproval(approvals);
    return approvalRepo.updateOrCreate(kApprovals);
  }

  @Override
  public boolean revokeApprovals(
      final Collection<org.springframework.security.oauth2.provider.approval.Approval> approvals) {
    boolean success = true;

    final Collection<Approval> mongoApprovals = transformToKApproval(approvals);

    for (final Approval approval : mongoApprovals) {
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
  public Collection<org.springframework.security.oauth2.provider.approval.Approval> getApprovals(
      final String userId, final String clientId) {
    final List<Approval> approvals = approvalRepo.findByUserIdAndClientId(userId, clientId);
    return transformToApprovals(approvals);
  }

  private List<org.springframework.security.oauth2.provider.approval.Approval> transformToApprovals(
      final List<Approval> approvals) {
    return approvals
        .stream()
        .map(
            kApproval ->
                new org.springframework.security.oauth2.provider.approval.Approval(
                    kApproval.getUserName(),
                    kApproval.getClientId(),
                    kApproval.getScope(),
                    Date.from(kApproval.getExpiresAt().atZone(ZoneId.systemDefault()).toInstant()),
                    kApproval.getStatus(),
                    Date.from(
                        kApproval.getLastUpdatedAt().atZone(ZoneId.systemDefault()).toInstant())))
        .collect(Collectors.toList());
  }

  private List<Approval> transformToKApproval(
      final Collection<org.springframework.security.oauth2.provider.approval.Approval> approvals) {
    return approvals
        .stream()
        .map(
            approval ->
                new Approval(
                    UUID.randomUUID().toString(),
                    approval.getUserId(),
                    approval.getClientId(),
                    approval.getScope(),
                    isNull(approval.getStatus())
                        ? org.springframework.security.oauth2.provider.approval.Approval
                            .ApprovalStatus.APPROVED
                        : approval.getStatus(),
                    LocalDateTimeUtil.convertTolocalDateTimeFrom(approval.getExpiresAt()),
                    LocalDateTimeUtil.convertTolocalDateTimeFrom(approval.getLastUpdatedAt())))
        .collect(Collectors.toList());
  }

  public void setHandleRevocationsAsExpiry(boolean handleRevocationsAsExpiry) {
    this.handleRevocationsAsExpiry = handleRevocationsAsExpiry;
  }
}
