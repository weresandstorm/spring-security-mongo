package me.konglong.springsecurity.service;

import static me.konglong.springsecurity.commons.SecurityRDG.list;
import static me.konglong.springsecurity.commons.SecurityRDG.ofApproval;
import static me.konglong.springsecurity.commons.SecurityRDG.ofMongoApproval;
import static me.konglong.springsecurity.commons.SecurityRDG.string;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import me.konglong.springsecurity.domain.ApprovalRepo;
import me.konglong.springsecurity.domain.KApproval;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.oauth2.provider.approval.Approval;

@RunWith(MockitoJUnitRunner.class)
public class ApprovalStoreTest {

  @Mock private ApprovalRepo approvalRepo;

  private KApprovalStore approvalStore;

  @Before
  public void setup() {
    approvalStore = new KApprovalStore(approvalRepo);
  }

  @Test
  public void shouldAddApprovals() {
    // Given
    final List<Approval> approvals = list(ofApproval()).next();

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
    final List<Approval> approvals = list(ofApproval()).next();

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
    final List<Approval> approvals = list(ofApproval()).next();

    // And
    approvalStore.setHandleRevocationsAsExpiry(false);

    // And
    given(approvalRepo.deleteByUserIdAndClientIdAndScope(any(KApproval.class))).willReturn(true);

    // When
    final boolean result = approvalStore.revokeApprovals(approvals);

    // Then
    assertThat(result).isTrue();
    verify(approvalRepo, never()).updateExpiresAt(any(LocalDateTime.class), any(KApproval.class));
  }

  @Test
  public void shouldRevokeApprovalsByUpdateWhenHandleRevocationsAsExpiryIsTrue() {
    // Given
    final List<Approval> approvals = list(ofApproval()).next();

    // And
    approvalStore.setHandleRevocationsAsExpiry(true);

    // And
    given(approvalRepo.updateExpiresAt(any(LocalDateTime.class), any(KApproval.class)))
        .willReturn(true);

    // When
    final boolean result = approvalStore.revokeApprovals(approvals);

    // Then
    assertThat(result).isTrue();
    verify(approvalRepo, never()).deleteByUserIdAndClientIdAndScope(any(KApproval.class));
  }

  @Test
  public void shouldGetApprovals() {
    // Given
    final String userId = string().next();
    final String clientId = string().next();

    // And
    final List<KApproval> expectedApprovals = list(ofMongoApproval()).next();
    given(approvalRepo.findByUserIdAndClientId(userId, clientId)).willReturn(expectedApprovals);

    // When
    final Collection<Approval> approvals = approvalStore.getApprovals(userId, clientId);

    // Then
    assertThat(approvals).hasSameSizeAs(expectedApprovals);
  }
}
