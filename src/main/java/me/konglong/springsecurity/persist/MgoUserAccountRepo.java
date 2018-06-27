/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package me.konglong.springsecurity.persist;

import static com.mongodb.client.model.Updates.set;

import java.util.Optional;
import me.konglong.springsecurity.domain.KUserAccount;
import me.konglong.springsecurity.domain.UserAccountRepo;
import org.springframework.stereotype.Component;

@Component
public class MgoUserAccountRepo extends MgoRepoBase<KUserAccount, String>
    implements UserAccountRepo {

  @Override
  public String collectionName() {
    return "user_accounts";
  }

  @Override
  public Optional<KUserAccount> findByUsername(String username) {
    return getOptional(username);
  }

  @Override
  public boolean changePassword(String oldPassword, String newPassword, String username) {
    KUserAccount account = get(username);
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
