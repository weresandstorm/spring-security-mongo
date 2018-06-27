/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package me.konglong.springsecurity.persist;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

import java.util.List;
import me.konglong.springsecurity.domain.KAccessToken;
import me.konglong.springsecurity.domain.AccessTokenRepo;
import org.springframework.stereotype.Component;

@Component
public class MgoAccessTokenRepo extends MgoRepoBase<KAccessToken, String> implements
    AccessTokenRepo {

  @Override
  public String collectionName() {
    return "access_tokens";
  }

  @Override
  public KAccessToken findByTokenId(String tokenId) {
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
  public KAccessToken findByAuthenticationId(String authenticationId) {
    return findOne(eq("authenticationId", authenticationId));
  }

  @Override
  public List<KAccessToken> findByUsernameAndClientId(String username, String clientId) {
    return find(and(
        eq("username", username),
        eq("clientId", clientId)
    ));
  }

  @Override
  public List<KAccessToken> findByClientId(String clientId) {
    return find(eq("clientId", clientId));
  }
}
