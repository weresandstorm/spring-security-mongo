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

package io.sandstorm.springsecurity.domain;

import static java.util.Objects.isNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

public class ConcreteClientDetails implements ClientDetails, Entity<String> {

  @BsonId public String clientId;
  public String clientSecret;
  public Set<String> scope = Collections.emptySet();
  public Set<String> resourceIds = Collections.emptySet();
  public Set<String> authorizedGrantTypes = Collections.emptySet();
  public Set<String> registeredRedirectUris;
  public List<GrantedAuthority> grantedAuthorities = Collections.emptyList();
  public Integer accessTokenValiditySeconds;
  public Integer refreshTokenValiditySeconds;
  public Map<String, String> additionalInfo = new HashMap<>();
  public Set<String> autoApproveScopes;

  public ConcreteClientDetails() {}

  public ConcreteClientDetails(
      final String clientId,
      final String clientSecret,
      final Set<String> scope,
      final Set<String> resourceIds,
      final Set<String> authorizedGrantTypes,
      final Set<String> registeredRedirectUris,
      final List<GrantedAuthority> grantedAuthorities,
      final Integer accessTokenValiditySeconds,
      final Integer refreshTokenValiditySeconds,
      final Map<String, Object> additionalInfo,
      final Set<String> autoApproveScopes) {
    this.clientId = clientId;
    this.clientSecret = clientSecret;
    this.scope = scope;
    this.resourceIds = resourceIds;
    this.authorizedGrantTypes = authorizedGrantTypes;
    this.registeredRedirectUris = registeredRedirectUris;
    this.grantedAuthorities = grantedAuthorities;
    this.accessTokenValiditySeconds = accessTokenValiditySeconds;
    this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
    this.autoApproveScopes = autoApproveScopes;

    if (additionalInfo != null && !additionalInfo.isEmpty()) {
      Map<String, String> map = new HashMap<>();
      additionalInfo
          .entrySet()
          .forEach(
              entry ->
                  map.put(
                      entry.getKey(),
                      entry.getValue() == null ? null : entry.getValue().toString()));
      this.additionalInfo = map;
    }
  }

  @Override
  public String id() {
    return getClientId();
  }

  @Override
  public String getClientId() {
    return clientId;
  }

  @Override
  public String getClientSecret() {
    return clientSecret;
  }

  @Override
  public Set<String> getScope() {
    return scope;
  }

  @Override
  public Set<String> getResourceIds() {
    return resourceIds;
  }

  @Override
  public Set<String> getAuthorizedGrantTypes() {
    return authorizedGrantTypes;
  }

  @BsonIgnore
  @Override
  public Collection<GrantedAuthority> getAuthorities() {
    return grantedAuthorities;
  }

  @Override
  public Integer getAccessTokenValiditySeconds() {
    return accessTokenValiditySeconds;
  }

  @Override
  public Integer getRefreshTokenValiditySeconds() {
    return refreshTokenValiditySeconds;
  }

  public Map<String, String> getAdditionalInfo() {
    return additionalInfo;
  }

  @BsonIgnore
  @Override
  public Map<String, Object> getAdditionalInformation() {
    return (Map) additionalInfo;
  }

  public Set<String> getAutoApproveScopes() {
    return autoApproveScopes;
  }

  @BsonIgnore
  @Override
  public boolean isScoped() {
    return this.scope != null && !this.scope.isEmpty();
  }

  @BsonIgnore
  @Override
  public boolean isSecretRequired() {
    return this.clientSecret != null;
  }

  @BsonIgnore
  @Override
  public Set<String> getRegisteredRedirectUri() {
    return registeredRedirectUris;
  }

  @BsonIgnore
  @Override
  public boolean isAutoApprove(final String scope) {
    if (isNull(autoApproveScopes)) {
      return false;
    }
    for (String auto : autoApproveScopes) {
      if ("true".equals(auto) || scope.matches(auto)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        clientId,
        clientSecret,
        scope,
        resourceIds,
        authorizedGrantTypes,
        registeredRedirectUris,
        grantedAuthorities,
        accessTokenValiditySeconds,
        refreshTokenValiditySeconds,
        additionalInfo,
        autoApproveScopes);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final ConcreteClientDetails other = (ConcreteClientDetails) obj;
    return Objects.equals(this.clientId, other.clientId)
        && Objects.equals(this.scope, other.scope)
        && Objects.equals(this.resourceIds, other.resourceIds)
        && Objects.equals(this.authorizedGrantTypes, other.authorizedGrantTypes)
        && Objects.equals(this.registeredRedirectUris, other.registeredRedirectUris)
        && Objects.equals(this.grantedAuthorities, other.grantedAuthorities)
        && Objects.equals(this.accessTokenValiditySeconds, other.accessTokenValiditySeconds)
        && Objects.equals(this.refreshTokenValiditySeconds, other.refreshTokenValiditySeconds)
        && Objects.equals(this.additionalInfo, other.additionalInfo)
        && Objects.equals(this.autoApproveScopes, other.autoApproveScopes);
  }

  @Override
  public String toString() {
    return "ConcreteClientDetails{"
        + "id='"
        + clientId
        + '\''
        + ", clientSecret='***'"
        + ", scope="
        + scope
        + ", resourceIds="
        + resourceIds
        + ", authorizedGrantTypes="
        + authorizedGrantTypes
        + ", registeredRedirectUris="
        + registeredRedirectUris
        + ", grantedAuthorities="
        + grantedAuthorities
        + ", accessTokenValiditySeconds="
        + accessTokenValiditySeconds
        + ", refreshTokenValiditySeconds="
        + refreshTokenValiditySeconds
        + ", additionalInfo="
        + additionalInfo
        + ", autoApproveScopes="
        + autoApproveScopes
        + '}';
  }
}
