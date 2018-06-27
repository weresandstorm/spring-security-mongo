/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package me.konglong.springsecurity.domain;

public interface RefreshTokenRepo extends Repo<KRefreshToken, String> {

  KRefreshToken findByTokenId(String tokenId);

  boolean deleteByTokenId(String tokenId);

}
