/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package me.konglong.springsecurity.domain;

public interface ClientDetailsRepo extends Repo<KClientDetails, String> {

  boolean deleteByClientId(String clientId);

  boolean update(KClientDetails concreteClientDetails);

  boolean updateClientSecret(String clientId, String newSecret);

  KClientDetails findByClientId(String clientId);
}
