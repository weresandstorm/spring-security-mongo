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

import static io.sandstorm.springsecurity.commons.SecurityRDG.integer;
import static io.sandstorm.springsecurity.commons.SecurityRDG.string;
import static io.sandstorm.springsecurity.commons.SecurityRDG.value;

import org.springframework.security.oauth2.provider.approval.Approval;

public class Oauth2ApprovalBuilder {

  private String userId = string().next();
  private String clientId = string().next();
  private String scope = string().next();
  private Integer expiresIn = integer().next();
  private Approval.ApprovalStatus status = value(Approval.ApprovalStatus.class).next();

  private Oauth2ApprovalBuilder() {}

  public static Oauth2ApprovalBuilder approvalBuilder() {
    return new Oauth2ApprovalBuilder();
  }

  public Approval build() {
    return new Approval(userId, clientId, scope, expiresIn, status);
  }
}
