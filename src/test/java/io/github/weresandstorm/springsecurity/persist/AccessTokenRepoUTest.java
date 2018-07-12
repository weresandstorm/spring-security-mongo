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

import static com.lordofthejars.nosqlunit.core.LoadStrategyEnum.CLEAN_INSERT;
import static org.assertj.core.api.Assertions.assertThat;

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import io.github.weresandstorm.springsecurity.domain.AccessTokenRepo;
import org.junit.Test;

public class AccessTokenRepoUTest extends RepoUnitTestBase {

  protected AccessTokenRepo accessTokenRepo;

  {
    accessTokenRepo = new MgoAccessTokenRepo();
    ((MgoAccessTokenRepo) accessTokenRepo).initialize(mongoHolder);
  }

  @Test
  @UsingDataSet(
      locations = {"/seed-data/access-tokens.json"},
      loadStrategy = CLEAN_INSERT)
  public void shouldDeleteTokenByRefreshToken() {
    String tokenId = "49d855f31931b6063aa9e315e092f17f";
    final boolean result = accessTokenRepo.deleteByRefreshTokenId(tokenId);
    assertThat(result).isTrue();
  }
}
