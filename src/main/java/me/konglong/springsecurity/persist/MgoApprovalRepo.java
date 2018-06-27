/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package me.konglong.springsecurity.persist;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import me.konglong.springsecurity.domain.KApproval;
import me.konglong.springsecurity.domain.ApprovalRepo;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Component;

@Component
public class MgoApprovalRepo extends MgoRepoBase<KApproval, String> implements ApprovalRepo {

  @Override
  public String collectionName() {
    return "approvals";
  }

  @Override
  public boolean updateOrCreate(Collection<KApproval> approvals) {
    boolean result = true;
    for (KApproval approval : approvals) {
      final Bson update =
          combine(
              set("expiresAt", approval.getExpiresAt()),
              set("status", approval.getStatus()),
              set("lastUpdatedAt", approval.getLastUpdatedAt()));
      boolean ok = insertOrUpdate(filterByUsernameAndClientIdAndScope(approval), update);
      if (!ok) {
        result = false;
      }
    }
    return result;
  }

  @Override
  public boolean updateExpiresAt(LocalDateTime expiresAt, KApproval approval) {
    return updateOne(filterByUsernameAndClientIdAndScope(approval), set("expiresAt", expiresAt));
  }

  @Override
  public boolean deleteByUserIdAndClientIdAndScope(KApproval approval) {
    return delete(filterByUsernameAndClientIdAndScope(approval));
  }

  @Override
  public List<KApproval> findByUserIdAndClientId(String userName, String clientId) {
    return find(and(eq("userName", userName), eq("clientId", clientId)));
  }

  private Bson filterByUsernameAndClientIdAndScope(final KApproval approval) {
    return and(
        eq("userName", approval.getUserName()),
        eq("clientId", approval.getClientId()),
        eq("scope", approval.getScope()));
  }
}
