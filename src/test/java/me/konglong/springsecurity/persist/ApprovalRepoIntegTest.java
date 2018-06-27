/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package me.konglong.springsecurity.persist;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.org.fyodor.generators.RDG.string;

import java.util.List;
import me.konglong.springsecurity.builders.KApprovalBuilder;
import me.konglong.springsecurity.domain.ApprovalRepo;
import me.konglong.springsecurity.domain.KApproval;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PersistConf.class)
@TestPropertySource("classpath:mongo.properties")
public class ApprovalRepoIntegTest {

  @Autowired private ApprovalRepo approvalRepo;

  @Test
  public void shouldSaveKApproval() {
    // Given
    final String clientId = string().next();
    final String userId = string().next();
    final KApproval kApproval =
        KApprovalBuilder.kApprovalBuilder().clientId(clientId).userId(userId).build();

    // When
    approvalRepo.save(kApproval);

    // Then
    final List<KApproval> results = approvalRepo.findByUserIdAndClientId(userId, clientId);
    assertThat(results).isNotEmpty();
    final KApproval approval = results.get(0);
    assertThat(approval).isEqualToComparingFieldByField(approval);
  }
}
