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

package io.sandstorm.springsecurity.builders;

import static io.sandstorm.springsecurity.commons.SecurityRDG.localDateTime;
import static uk.org.fyodor.generators.RDG.string;
import static uk.org.fyodor.generators.RDG.value;

import io.sandstorm.springsecurity.domain.Approval;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.security.oauth2.provider.approval.Approval.ApprovalStatus;

public class ApprovalBuilder {

  private String id = UUID.randomUUID().toString();
  private String userId = string().next();
  private String clientId = string().next();
  private String scope = string().next();
  private ApprovalStatus status = value(ApprovalStatus.class).next();
  private LocalDateTime expiresAt = localDateTime().next();
  private LocalDateTime lastUpdatedAt = localDateTime().next();

  private ApprovalBuilder() {}

  public static ApprovalBuilder kApprovalBuilder() {
    return new ApprovalBuilder();
  }

  public Approval build() {
    return new Approval(id, userId, clientId, scope, status, expiresAt, lastUpdatedAt);
  }

  public ApprovalBuilder clientId(String clientId) {
    this.clientId = clientId;
    return this;
  }

  public ApprovalBuilder userId(String userId) {
    this.userId = userId;
    return this;
  }
}
