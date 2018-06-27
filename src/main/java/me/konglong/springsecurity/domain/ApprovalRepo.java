/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package me.konglong.springsecurity.domain;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface ApprovalRepo extends Repo<KApproval, String> {

  boolean updateOrCreate(Collection<KApproval> approvals);

  boolean updateExpiresAt(LocalDateTime expiresAt, KApproval approval);

  boolean deleteByUserIdAndClientIdAndScope(KApproval approval);

  List<KApproval> findByUserIdAndClientId(String userName, String clientId);
}
