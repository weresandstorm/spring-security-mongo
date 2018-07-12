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

package io.github.weresandstorm.springsecurity.persist;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

import io.github.weresandstorm.springsecurity.domain.AccessToken;
import io.github.weresandstorm.springsecurity.domain.AccessTokenRepo;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class MgoAccessTokenRepo extends MgoRepoBase<AccessToken, String>
    implements AccessTokenRepo {

  @Override
  public String collectionName() {
    return "access_tokens";
  }

  @Override
  public AccessToken findByTokenId(String tokenId) {
    return get(tokenId);
  }

  @Override
  public boolean deleteByTokenId(String tokenId) {
    return delete(tokenId);
  }

  @Override
  public boolean deleteByRefreshTokenId(String refreshTokenId) {
    return delete(eq("refreshTokenId", refreshTokenId));
  }

  @Override
  public AccessToken findByAuthenticationId(String authenticationId) {
    return findOne(eq("authenticationId", authenticationId));
  }

  @Override
  public List<AccessToken> findByUsernameAndClientId(String username, String clientId) {
    return find(and(eq("username", username), eq("clientId", clientId)));
  }

  @Override
  public List<AccessToken> findByClientId(String clientId) {
    return find(eq("clientId", clientId));
  }
}
