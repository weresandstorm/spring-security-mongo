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

import io.github.weresandstorm.springsecurity.domain.RefreshToken;
import uk.org.fyodor.generators.RDG;

public class RefreshTokenBuilder {

  private String tokenId = RDG.string().next();
  private byte[] token = RDG.string().next().getBytes();
  private byte[] authentication = RDG.string().next().getBytes();

  private RefreshTokenBuilder() {}

  public static RefreshTokenBuilder kRefreshTokenBuilder() {
    return new RefreshTokenBuilder();
  }

  public RefreshToken build() {
    return new RefreshToken(tokenId, token, authentication);
  }

  public RefreshTokenBuilder token(final byte[] oAuth2RefreshTokenSer) {
    this.token = oAuth2RefreshTokenSer;
    return this;
  }

  public RefreshTokenBuilder authentication(final byte[] authenticationSer) {
    this.authentication = authenticationSer;
    return this;
  }
}
