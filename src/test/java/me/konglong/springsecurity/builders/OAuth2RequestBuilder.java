package me.konglong.springsecurity.builders;

import static me.konglong.springsecurity.commons.SecurityRDG.ofGrantedAuthority;
import static me.konglong.springsecurity.commons.SecurityRDG.serializableOf;
import static uk.org.fyodor.generators.RDG.bool;
import static uk.org.fyodor.generators.RDG.list;
import static uk.org.fyodor.generators.RDG.longVal;
import static uk.org.fyodor.generators.RDG.map;
import static uk.org.fyodor.generators.RDG.set;
import static uk.org.fyodor.generators.RDG.string;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.OAuth2Request;

public class OAuth2RequestBuilder {

  private Map<String, String> requestParameters = map(string(), string()).next();
  private String clientId = string().next();
  private Collection<? extends GrantedAuthority> authorities = list(ofGrantedAuthority()).next();
  private boolean approved = bool().next();
  private Set<String> scope = set(string()).next();
  private Set<String> resourceIds = set(string()).next();
  private String redirectUri = string().next();
  private Set<String> responseTypes = set(string()).next();
  private Map<String, Serializable> extensionProperties =
      map(string(), serializableOf(longVal())).next();

  private OAuth2RequestBuilder() {}

  public static OAuth2RequestBuilder oAuth2RequestBuilder() {
    return new OAuth2RequestBuilder();
  }

  public OAuth2Request build() {
    return new OAuth2Request(
        requestParameters,
        clientId,
        authorities,
        approved,
        scope,
        resourceIds,
        redirectUri,
        responseTypes,
        extensionProperties);
  }
}
