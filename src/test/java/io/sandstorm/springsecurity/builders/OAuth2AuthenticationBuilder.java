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

import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import uk.org.fyodor.generators.RDG;

public class OAuth2AuthenticationBuilder {

  private OAuth2AuthenticationBuilder() {}

  public static OAuth2AuthenticationBuilder oAuth2AuthenticationBuilder() {
    return new OAuth2AuthenticationBuilder();
  }

  public OAuth2Authentication build() {
    return new OAuth2Authentication(
        OAuth2RequestBuilder.oAuth2RequestBuilder().build(),
        new TestingAuthenticationToken(
            UserIdentityBuilder.userIdentityBuilder().build(), RDG.string().next()));
  }
}
