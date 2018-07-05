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

import static uk.org.fyodor.generators.RDG.map;
import static uk.org.fyodor.generators.RDG.set;
import static uk.org.fyodor.generators.RDG.string;

import io.sandstorm.springsecurity.commons.SecurityRDG;
import io.sandstorm.springsecurity.util.LocalDateTimeUtil;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;

public class OAuth2AccessTokenBuilder {

  private LocalDateTime expiration = SecurityRDG.localDateTime().next();
  private OAuth2RefreshToken oAuth2RefreshToken =
      OAuth2RefreshTokenBuilder.oAuth2RefreshToken().build();
  private Set<String> scope = set(string()).next();
  private Map<String, Object> additionalInformation =
      map(string(), SecurityRDG.objectOf(string())).next();
  private String token = string().next();

  private OAuth2AccessTokenBuilder() {}

  public static OAuth2AccessTokenBuilder oAuth2AccessTokenBuilder() {
    return new OAuth2AccessTokenBuilder();
  }

  public OAuth2AccessToken build() {
    final DefaultOAuth2AccessToken oAuth2AccessToken = new DefaultOAuth2AccessToken(token);
    oAuth2AccessToken.setExpiration(LocalDateTimeUtil.convertToDateFrom(expiration));
    oAuth2AccessToken.setRefreshToken(oAuth2RefreshToken);
    oAuth2AccessToken.setScope(scope);
    oAuth2AccessToken.setAdditionalInformation(additionalInformation);
    return oAuth2AccessToken;
  }

  public OAuth2AccessTokenBuilder token(String token) {
    this.token = token;
    return this;
  }
}
