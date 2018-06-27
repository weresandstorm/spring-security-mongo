/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package me.konglong.springsecurity.persist;

import org.junit.AfterClass;

public abstract class RepoUnitTestBase {

  protected static FongoProvider mongoProvider;
  protected static MongoHolder mongoHolder;

  public static void setUpClass() {
    MongoConfProps mgoConfProps = new MongoConfProps();
    mgoConfProps.host = "localhost";
    mgoConfProps.port = 27017;
    mgoConfProps.database = "security";

    mongoProvider = new FongoProvider(mgoConfProps);
    mongoHolder = new MongoHolder(mongoProvider);
    System.out.println("Before Class");
  }

  @AfterClass
  public static void tearDownClass() {
    mongoProvider.cleanUp();
    System.out.println("After Class");
  }
}
