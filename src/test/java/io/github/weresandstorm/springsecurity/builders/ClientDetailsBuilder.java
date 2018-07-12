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

package io.github.weresandstorm.springsecurity.builders;

import static io.github.weresandstorm.springsecurity.commons.SecurityRDG.integer;
import static io.github.weresandstorm.springsecurity.commons.SecurityRDG.list;
import static io.github.weresandstorm.springsecurity.commons.SecurityRDG.map;
import static io.github.weresandstorm.springsecurity.commons.SecurityRDG.objectOf;
import static io.github.weresandstorm.springsecurity.commons.SecurityRDG.ofEscapedString;
import static io.github.weresandstorm.springsecurity.commons.SecurityRDG.ofGrantedAuthority;
import static io.github.weresandstorm.springsecurity.commons.SecurityRDG.set;

import com.google.common.collect.Sets;
import io.github.weresandstorm.springsecurity.domain.ConcreteClientDetails;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;

public class ClientDetailsBuilder {

  private String clientId = ofEscapedString().next();
  private String clientSecret = ofEscapedString().next();
  private Set<String> scope = set(ofEscapedString()).next();
  private Set<String> resourceIds = set(ofEscapedString()).next();
  private Set<String> authorizedGrantTypes = set(ofEscapedString()).next();
  private Set<String> registeredRedirectUris = set(ofEscapedString()).next();
  private List<GrantedAuthority> authorities = list(ofGrantedAuthority()).next();
  private Integer accessTokenValiditySeconds = integer().next();
  private Integer refreshTokenValiditySeconds = integer().next();
  private Map<String, Object> additionalInfo =
      map(ofEscapedString(), objectOf(ofEscapedString())).next();

  private Set<String> autoApproveScopes = Sets.newHashSet("true");

  private ClientDetailsBuilder() {}

  public static ClientDetailsBuilder concreteClientDetailsBuilder() {
    return new ClientDetailsBuilder();
  }

  public ConcreteClientDetails build() {
    return new ConcreteClientDetails(
        clientId,
        clientSecret,
        scope,
        resourceIds,
        authorizedGrantTypes,
        registeredRedirectUris,
        authorities,
        accessTokenValiditySeconds,
        refreshTokenValiditySeconds,
        additionalInfo,
        autoApproveScopes);
  }
}
