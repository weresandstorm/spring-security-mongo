/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package me.konglong.springsecurity.persist;

import com.mongodb.client.MongoDatabase;

public interface MongoProvider {

  /**
   * Provides a {@link MongoDatabase} to store all security-related data into.
   *
   * @return a {@link MongoDatabase} instance.
   */
  MongoDatabase targetDb();
}
