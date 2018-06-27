package me.konglong.springsecurity.domain;

import static java.util.Objects.isNull;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

public class KClientDetails implements ClientDetails, Entity<String> {

  private String id;
  private String clientSecret;
  private Set<String> scope = Collections.emptySet();
  private Set<String> resourceIds = Collections.emptySet();
  private Set<String> authorizedGrantTypes = Collections.emptySet();
  private Set<String> registeredRedirectUris;
  private List<GrantedAuthority> authorities = Collections.emptyList();
  private Integer accessTokenValiditySeconds;
  private Integer refreshTokenValiditySeconds;
  private Map<String, Object> additionalInfo = new LinkedHashMap<>();
  private Set<String> autoApproveScopes;

  public KClientDetails() {}

  @BsonCreator
  public KClientDetails(
      final String id,
      final String clientSecret,
      final Set<String> scope,
      final Set<String> resourceIds,
      final Set<String> authorizedGrantTypes,
      final Set<String> registeredRedirectUris,
      final List<GrantedAuthority> authorities,
      final Integer accessTokenValiditySeconds,
      final Integer refreshTokenValiditySeconds,
      final Map<String, Object> additionalInfo,
      final Set<String> autoApproveScopes) {
    this.id = id;
    this.clientSecret = clientSecret;
    this.scope = scope;
    this.resourceIds = resourceIds;
    this.authorizedGrantTypes = authorizedGrantTypes;
    this.registeredRedirectUris = registeredRedirectUris;
    this.authorities = authorities;
    this.accessTokenValiditySeconds = accessTokenValiditySeconds;
    this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
    this.additionalInfo = additionalInfo;
    this.autoApproveScopes = autoApproveScopes;
  }

  @Override
  public String id() {
    return id;
  }

  @Override
  public String getClientId() {
    return id();
  }

  public String getClientSecret() {
    return clientSecret;
  }

  public Set<String> getScope() {
    return scope;
  }

  public Set<String> getResourceIds() {
    return resourceIds;
  }

  public Set<String> getAuthorizedGrantTypes() {
    return authorizedGrantTypes;
  }

  public List<GrantedAuthority> getAuthorities() {
    return authorities;
  }

  public Integer getAccessTokenValiditySeconds() {
    return accessTokenValiditySeconds;
  }

  public Integer getRefreshTokenValiditySeconds() {
    return refreshTokenValiditySeconds;
  }

  public Map<String, Object> getAdditionalInformation() {
    return additionalInfo;
  }

  public void setAutoApproveScopes(final Set<String> autoApproveScopes) {
    this.autoApproveScopes = autoApproveScopes;
  }

  public Set<String> getAutoApproveScopes() {
    return autoApproveScopes;
  }

  @Override
  public boolean isScoped() {
    return this.scope != null && !this.scope.isEmpty();
  }

  @Override
  public boolean isSecretRequired() {
    return this.clientSecret != null;
  }

  @Override
  public Set<String> getRegisteredRedirectUri() {
    return registeredRedirectUris;
  }

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
        id,
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

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final KClientDetails other = (KClientDetails) obj;
    return Objects.equals(this.id, other.id)
        && Objects.equals(this.scope, other.scope)
        && Objects.equals(this.resourceIds, other.resourceIds)
        && Objects.equals(this.authorizedGrantTypes, other.authorizedGrantTypes)
        && Objects.equals(this.registeredRedirectUris, other.registeredRedirectUris)
        && Objects.equals(this.authorities, other.authorities)
        && Objects.equals(this.accessTokenValiditySeconds, other.accessTokenValiditySeconds)
        && Objects.equals(this.refreshTokenValiditySeconds, other.refreshTokenValiditySeconds)
        && Objects.equals(this.additionalInfo, other.additionalInfo)
        && Objects.equals(this.autoApproveScopes, other.autoApproveScopes);
  }

  @Override
  public String toString() {
    return "KClientDetails{"
        + "id='"
        + id
        + '\''
        + ", clientSecret='"
        + clientSecret
        + '\''
        + ", scope="
        + scope
        + ", resourceIds="
        + resourceIds
        + ", authorizedGrantTypes="
        + authorizedGrantTypes
        + ", registeredRedirectUris="
        + registeredRedirectUris
        + ", authorities="
        + authorities
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
