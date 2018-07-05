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

package io.sandstorm.springsecurity.commons;

import io.sandstorm.springsecurity.builders.AccessTokenBuilder;
import io.sandstorm.springsecurity.builders.ApprovalBuilder;
import io.sandstorm.springsecurity.builders.Oauth2ApprovalBuilder;
import io.sandstorm.springsecurity.domain.AccessToken;
import io.sandstorm.springsecurity.domain.Approval;
import java.io.Serializable;
import java.time.LocalDateTime;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import uk.org.fyodor.generators.Generator;
import uk.org.fyodor.generators.characters.CharacterSetFilter;

public class SecurityRDG extends uk.org.fyodor.generators.RDG {

  public static Generator<String> ofEscapedString() {
    return () -> string(30, CharacterSetFilter.LettersAndDigits).next();
  }

  public static Generator<GrantedAuthority> ofGrantedAuthority() {
    return () -> new SimpleGrantedAuthority(string().next());
  }

  public static Generator<GrantedAuthority> ofInvalidAuthority() {
    return () -> new SimpleGrantedAuthority("");
  }

  public static Generator<AccessToken> ofMongoOAuth2AccessToken() {
    return () -> AccessTokenBuilder.kAccessTokenBuilder().build();
  }

  public static Generator<Object> objectOf(final Generator generator) {
    return () -> generator.next();
  }

  public static Generator<Serializable> serializableOf(
      final Generator<? extends Serializable> generator) {
    return () -> generator.next();
  }

  public static Generator<org.springframework.security.oauth2.provider.approval.Approval>
      ofApproval() {
    return () -> Oauth2ApprovalBuilder.approvalBuilder().build();
  }

  public static Generator<Approval> ofMongoApproval() {
    return () -> ApprovalBuilder.kApprovalBuilder().build();
  }

  public static Generator<LocalDateTime> localDateTime() {
    return () -> LocalDateTime.now().minusDays(longVal(30).next());
  }
}
