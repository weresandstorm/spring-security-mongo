/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package me.konglong.springsecurity.persist;

import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MongoHolder {

  private MongoDatabase targetDb;

  @Autowired
  public MongoHolder(MongoProvider mongoProvider) {
    targetDb = mongoProvider.targetDb();
  }

  /**
   * Returns the holded target {@link MongoDatabase} to store all security-related data into.
   *
   * @return a {@link MongoDatabase}
   */
  public MongoDatabase targetDb() {
    return targetDb;
  }
}
