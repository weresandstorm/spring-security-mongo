/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package me.konglong.springsecurity.domain;

public interface ClientTokenRepo extends Repo<KClientToken, String> {

  boolean deleteByAuthenticationId(String authenticationId);

  KClientToken findByAuthenticationId(String authenticationId);
}
