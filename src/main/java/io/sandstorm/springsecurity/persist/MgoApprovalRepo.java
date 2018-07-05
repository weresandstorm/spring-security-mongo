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

package io.sandstorm.springsecurity.persist;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

import io.sandstorm.springsecurity.domain.Approval;
import io.sandstorm.springsecurity.domain.ApprovalRepo;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Component;

@Component
public class MgoApprovalRepo extends MgoRepoBase<Approval, String> implements ApprovalRepo {

  @Override
  public String collectionName() {
    return "approvals";
  }

  @Override
  public boolean updateOrCreate(Collection<Approval> approvals) {
    boolean result = true;
    for (Approval approval : approvals) {
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
  public boolean updateExpiresAt(LocalDateTime expiresAt, Approval approval) {
    return updateOne(filterByUsernameAndClientIdAndScope(approval), set("expiresAt", expiresAt));
  }

  @Override
  public boolean deleteByUserIdAndClientIdAndScope(Approval approval) {
    return delete(filterByUsernameAndClientIdAndScope(approval));
  }

  @Override
  public List<Approval> findByUserIdAndClientId(String userName, String clientId) {
    return find(and(eq("userName", userName), eq("clientId", clientId)));
  }

  private Bson filterByUsernameAndClientIdAndScope(final Approval approval) {
    return and(
        eq("userName", approval.getUserName()),
        eq("clientId", approval.getClientId()),
        eq("scope", approval.getScope()));
  }
}
