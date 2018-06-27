package me.konglong.springsecurity.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import me.konglong.springsecurity.builders.KClientDetailsBuilder;
import me.konglong.springsecurity.config.ApplicationConfiguration;
import me.konglong.springsecurity.domain.ClientDetailsRepo;
import me.konglong.springsecurity.domain.KClientDetails;
import me.konglong.springsecurity.persist.PersistConf;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = {ApplicationConfiguration.class, PersistConf.class})
@ActiveProfiles("test")
@DirtiesContext
public class ClientDetailsServiceIntegTest {

  @Autowired private KClientDetailsService clientDetailsService;

  @Autowired private ClientDetailsRepo clientDetailsRepo;

  @Test
  public void shouldPersistClientDetailsSuccessfully() {
    // Given
    final KClientDetails clientDetails = KClientDetailsBuilder.kClientDetailsBuilder().build();

    // When
    clientDetailsService.addClientDetails(clientDetails);

    // Then
    final KClientDetails expectedClientDetails =
        clientDetailsRepo.findByClientId(clientDetails.getClientId());
    assertThat(expectedClientDetails).isNotNull();
    assertThat(expectedClientDetails).isEqualTo(clientDetails);
  }

  @Test
  public void shouldLoadClientDetailsByIdSuccessfully() {
    // Given
    final KClientDetails clientDetails = KClientDetailsBuilder.kClientDetailsBuilder().build();

    // And
    clientDetailsService.addClientDetails(clientDetails);

    // When
    final ClientDetails expectedClientDetails =
        clientDetailsService.loadClientByClientId(clientDetails.getClientId());

    // Then
    assertThat(expectedClientDetails).isNotNull();
    assertThat(expectedClientDetails).isEqualTo(clientDetails);
  }

  @Test
  public void shouldGetListOfClientDetailsByIdSuccessfully() {
    // Given
    final KClientDetails clientDetails = KClientDetailsBuilder.kClientDetailsBuilder().build();

    // And
    clientDetailsService.addClientDetails(clientDetails);

    // When
    final List<ClientDetails> expectedClientDetails = clientDetailsService.listClientDetails();

    // Then
    assertThat(expectedClientDetails).contains(clientDetails);
  }
}
