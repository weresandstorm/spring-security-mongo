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

import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import uk.org.fyodor.generators.RDG;

public class OAuth2RefreshTokenBuilder {

  private String value = RDG.string().next();

  private OAuth2RefreshTokenBuilder() {}

  public static OAuth2RefreshTokenBuilder oAuth2RefreshToken() {
    return new OAuth2RefreshTokenBuilder();
  }

  public OAuth2RefreshTokenBuilder value(String value) {
    this.value = value;
    return this;
  }

  public OAuth2RefreshToken build() {
    return new DefaultOAuth2RefreshToken(value);
  }
}
