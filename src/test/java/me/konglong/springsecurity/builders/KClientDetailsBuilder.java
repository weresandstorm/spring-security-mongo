package me.konglong.springsecurity.builders;

import static me.konglong.springsecurity.commons.SecurityRDG.integer;
import static me.konglong.springsecurity.commons.SecurityRDG.list;
import static me.konglong.springsecurity.commons.SecurityRDG.map;
import static me.konglong.springsecurity.commons.SecurityRDG.objectOf;
import static me.konglong.springsecurity.commons.SecurityRDG.ofEscapedString;
import static me.konglong.springsecurity.commons.SecurityRDG.ofGrantedAuthority;
import static me.konglong.springsecurity.commons.SecurityRDG.set;

import com.google.common.collect.Sets;
import java.util.List;
import java.util.Map;
import java.util.Set;
import me.konglong.springsecurity.domain.KClientDetails;
import org.springframework.security.core.GrantedAuthority;

public class KClientDetailsBuilder {

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

  private KClientDetailsBuilder() {}

  public static KClientDetailsBuilder kClientDetailsBuilder() {
    return new KClientDetailsBuilder();
  }

  public KClientDetails build() {
    return new KClientDetails(
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
