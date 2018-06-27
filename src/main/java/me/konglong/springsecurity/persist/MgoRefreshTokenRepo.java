/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package me.konglong.springsecurity.persist;

import me.konglong.springsecurity.domain.KRefreshToken;
import me.konglong.springsecurity.domain.RefreshTokenRepo;
import org.springframework.stereotype.Component;

@Component
public class MgoRefreshTokenRepo extends MgoRepoBase<KRefreshToken, String> implements RefreshTokenRepo {

  @Override
  public String collectionName() {
    return "refresh_tokens";
  }

  @Override
  public KRefreshToken findByTokenId(String tokenId) {
    return get(tokenId);
  }

  @Override
  public boolean deleteByTokenId(String tokenId) {
    return delete(tokenId);
  }
}
