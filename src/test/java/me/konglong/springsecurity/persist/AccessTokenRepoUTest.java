/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package me.konglong.springsecurity.persist;

import static com.lordofthejars.nosqlunit.core.LoadStrategyEnum.CLEAN_INSERT;
import static org.assertj.core.api.Assertions.assertThat;

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import org.junit.BeforeClass;
import org.junit.Test;

public class AccessTokenRepoUTest extends RepoUnitTestBase {

  protected static MgoAccessTokenRepo accessTokenRepo;

  @BeforeClass
  public static void setUpClass() {
    RepoUnitTestBase.setUpClass();
    accessTokenRepo = new MgoAccessTokenRepo();
    accessTokenRepo.initialize(mongoHolder);
    System.out.println("Before class !!!!");
  }

  @Test
  @UsingDataSet(
      locations = {"/seed-data/access-tokens.json"},
      loadStrategy = CLEAN_INSERT)
  public void shouldDeleteTokenByRefreshToken() {
    String tokenId = "49d855f31931b6063aa9e315e092f17f";
    final boolean result = accessTokenRepo.deleteByRefreshTokenId(tokenId);
    assertThat(result).isTrue();
  }

}
