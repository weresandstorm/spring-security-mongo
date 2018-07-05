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

import static uk.org.fyodor.generators.RDG.bool;
import static uk.org.fyodor.generators.RDG.list;
import static uk.org.fyodor.generators.RDG.longVal;
import static uk.org.fyodor.generators.RDG.map;
import static uk.org.fyodor.generators.RDG.set;
import static uk.org.fyodor.generators.RDG.string;

import io.sandstorm.springsecurity.commons.SecurityRDG;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.OAuth2Request;

public class OAuth2RequestBuilder {

  private Map<String, String> requestParameters = map(string(), string()).next();
  private String clientId = string().next();
  private Collection<? extends GrantedAuthority> authorities =
      list(SecurityRDG.ofGrantedAuthority()).next();
  private boolean approved = bool().next();
  private Set<String> scope = set(string()).next();
  private Set<String> resourceIds = set(string()).next();
  private String redirectUri = string().next();
  private Set<String> responseTypes = set(string()).next();
  private Map<String, Serializable> extensionProperties =
      map(string(), SecurityRDG.serializableOf(longVal())).next();

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
