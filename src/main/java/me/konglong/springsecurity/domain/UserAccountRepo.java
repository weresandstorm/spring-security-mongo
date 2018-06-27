/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package me.konglong.springsecurity.domain;

import java.util.Optional;

public interface UserAccountRepo extends Repo<KUserAccount, String> {

  Optional<KUserAccount> findByUsername(String username);

  boolean changePassword(String oldPassword, String newPassword, String username);

  void deleteByUsername(String username);
}
