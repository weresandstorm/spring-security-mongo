/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package me.konglong.springsecurity.persist;


import static org.assertj.core.api.Assertions.assertThat;
import static uk.org.fyodor.generators.RDG.string;

import java.util.Optional;
import me.konglong.springsecurity.builders.KUserAccountBuilder;
import me.konglong.springsecurity.domain.KUserAccount;
import me.konglong.springsecurity.domain.UserAccountRepo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PersistConf.class)
@TestPropertySource("classpath:mongo.properties")
public class UserAccountIntegTest {

  @Autowired
  private UserAccountRepo userAccountRepo;

  @Test
  public void shouldSaveUser() {
    //Given
    KUserAccount account = KUserAccountBuilder.userAccountBuilder().build();

    //When
    KUserAccount result = userAccountRepo.save(account);

    //Then
    Optional<KUserAccount> expectedAccount = userAccountRepo.findByUsername(account.getUsername());
    assertThat(result).isEqualTo(expectedAccount.get());
  }

  @Test
  public void shouldChangePasswordUser() {
    //Given
    KUserAccount account = KUserAccountBuilder.userAccountBuilder().build();
    userAccountRepo.save(account);

    //When
    final boolean result = userAccountRepo
        .changePassword(account.getPassword(), string().next(), account.getUsername());

    //Then
    assertThat(result).isTrue();
  }
}