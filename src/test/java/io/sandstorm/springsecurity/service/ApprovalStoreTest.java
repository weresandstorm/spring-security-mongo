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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import io.sandstorm.springsecurity.commons.SecurityRDG;
import io.sandstorm.springsecurity.domain.Approval;
import io.sandstorm.springsecurity.domain.ApprovalRepo;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.org.fyodor.generators.RDG;

@RunWith(MockitoJUnitRunner.class)
public class ApprovalStoreTest {

  @Mock private ApprovalRepo approvalRepo;

  private ConcreteApprovalStore approvalStore;

  @Before
  public void setup() {
    approvalStore = new ConcreteApprovalStore(approvalRepo);
  }

  @Test
  public void shouldAddApprovals() {
    // Given
    final List<org.springframework.security.oauth2.provider.approval.Approval> approvals =
        RDG.list(SecurityRDG.ofApproval()).next();

    // And
    given(approvalRepo.updateOrCreate(anyCollection())).willReturn(true);

    // When
    final boolean result = approvalStore.addApprovals(approvals);

    // Then
    assertThat(result).isTrue();
  }

  @Test
  public void shouldReturnFalseWhenSomeApprovalsFailedToUpdateOrInsert() {
    // Given
    final List<org.springframework.security.oauth2.provider.approval.Approval> approvals =
        RDG.list(SecurityRDG.ofApproval()).next();

    // And
    given(approvalRepo.updateOrCreate(anyCollection())).willReturn(false);

    // When
    final boolean result = approvalStore.addApprovals(approvals);

    // Then
    assertThat(result).isFalse();
  }

  @Test
  public void shouldRevokeApprovalsByRemoveWhenHandleRevocationsAsExpiryIsFalse() {
    // Given
    final List<org.springframework.security.oauth2.provider.approval.Approval> approvals =
        RDG.list(SecurityRDG.ofApproval()).next();

    // And
    approvalStore.setHandleRevocationsAsExpiry(false);

    // And
    given(approvalRepo.deleteByUserIdAndClientIdAndScope(any(Approval.class))).willReturn(true);

    // When
    final boolean result = approvalStore.revokeApprovals(approvals);

    // Then
    assertThat(result).isTrue();
    verify(approvalRepo, never()).updateExpiresAt(any(LocalDateTime.class), any(Approval.class));
  }

  @Test
  public void shouldRevokeApprovalsByUpdateWhenHandleRevocationsAsExpiryIsTrue() {
    // Given
    final List<org.springframework.security.oauth2.provider.approval.Approval> approvals =
        RDG.list(SecurityRDG.ofApproval()).next();

    // And
    approvalStore.setHandleRevocationsAsExpiry(true);

    // And
    given(approvalRepo.updateExpiresAt(any(LocalDateTime.class), any(Approval.class)))
        .willReturn(true);

    // When
    final boolean result = approvalStore.revokeApprovals(approvals);

    // Then
    assertThat(result).isTrue();
    verify(approvalRepo, never()).deleteByUserIdAndClientIdAndScope(any(Approval.class));
  }

  @Test
  public void shouldGetApprovals() {
    // Given
    final String userId = RDG.string().next();
    final String clientId = RDG.string().next();

    // And
    final List<Approval> expectedApprovals = RDG.list(SecurityRDG.ofMongoApproval()).next();
    given(approvalRepo.findByUserIdAndClientId(userId, clientId)).willReturn(expectedApprovals);

    // When
    final Collection<org.springframework.security.oauth2.provider.approval.Approval> approvals =
        approvalStore.getApprovals(userId, clientId);

    // Then
    assertThat(approvals).hasSameSizeAs(expectedApprovals);
  }
}
