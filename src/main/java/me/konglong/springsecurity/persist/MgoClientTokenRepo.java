/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package me.konglong.springsecurity.persist;

import static com.mongodb.client.model.Filters.eq;

import me.konglong.springsecurity.domain.KClientToken;
import me.konglong.springsecurity.domain.ClientTokenRepo;
import org.springframework.stereotype.Component;

@Component
public class MgoClientTokenRepo extends MgoRepoBase<KClientToken, String>
    implements ClientTokenRepo {

  @Override
  public String collectionName() {
    return "client_tokens";
  }

  @Override
  public boolean deleteByAuthenticationId(String authenticationId) {
    return delete(eq("authenticationId", authenticationId));
  }

  @Override
  public KClientToken findByAuthenticationId(String authenticationId) {
    return findOne(eq("authenticationId", authenticationId));
  }
}
