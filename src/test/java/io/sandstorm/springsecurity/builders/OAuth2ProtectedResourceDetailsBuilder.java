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

import org.springframework.security.oauth2.client.resource.BaseOAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;

public class OAuth2ProtectedResourceDetailsBuilder {

  private OAuth2ProtectedResourceDetailsBuilder() {}

  public static OAuth2ProtectedResourceDetailsBuilder oAuth2ProtectedResourceDetailsBuilder() {
    return new OAuth2ProtectedResourceDetailsBuilder();
  }

  public OAuth2ProtectedResourceDetails build() {
    return new BaseOAuth2ProtectedResourceDetails();
  }
}
