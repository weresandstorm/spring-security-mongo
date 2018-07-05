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

package io.sandstorm.springsecurity.persist;

import io.sandstorm.springsecurity.domain.RefreshToken;
import io.sandstorm.springsecurity.domain.RefreshTokenRepo;
import org.springframework.stereotype.Component;

@Component
public class MgoRefreshTokenRepo extends MgoRepoBase<RefreshToken, String>
    implements RefreshTokenRepo {

  @Override
  public String collectionName() {
    return "refresh_tokens";
  }

  @Override
  public RefreshToken findByTokenId(String tokenId) {
    return get(tokenId);
  }

  @Override
  public boolean deleteByTokenId(String tokenId) {
    return delete(tokenId);
  }
}
