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

import static com.mongodb.client.model.Updates.set;

import io.github.weresandstorm.springsecurity.domain.Account;
import io.github.weresandstorm.springsecurity.domain.AccountRepo;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class MgoAccountRepo extends MgoRepoBase<Account, String> implements AccountRepo {

  @Override
  public String collectionName() {
    return "accounts";
  }

  @Override
  public Optional<Account> findByUsername(String username) {
    return getOptional(username);
  }

  @Override
  public boolean changePassword(String oldPassword, String newPassword, String username) {
    Account account = get(username);
    // TODO This is business logic and should be moved into domain service.
    if (!account.getPassword().equals(oldPassword)) {
      throw new RuntimeException("Old password is incorrect");
    }
    return update(username, set("password", newPassword));
  }

  @Override
  public void deleteByUsername(String username) {
    delete(username);
  }
}
