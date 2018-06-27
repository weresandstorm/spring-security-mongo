package me.konglong.springsecurity.service;

import static me.konglong.springsecurity.commons.SecurityRDG.string;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import me.konglong.springsecurity.builders.ClientDetailsBuilder;
import me.konglong.springsecurity.builders.KClientDetailsBuilder;
import me.konglong.springsecurity.domain.ClientDetailsRepo;
import me.konglong.springsecurity.domain.KClientDetails;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.NoSuchClientException;

@RunWith(MockitoJUnitRunner.class)
public class ClientDetailsServiceTest {

  @Mock private ClientDetailsRepo clientDetailsRepo;

  @Mock private PasswordEncoder passwordEncoder;

  private KClientDetailsService clientDetailsService;

  @Before
  public void setup() {
    clientDetailsService = new KClientDetailsService(clientDetailsRepo, passwordEncoder);
  }

  @Test
  public void shouldAddClientDetails() {
    // Given
    final ClientDetails clientDetails = ClientDetailsBuilder.clientDetailsBuilder().build();

    // When
    clientDetailsService.addClientDetails(clientDetails);

    // Then
    verify(clientDetailsRepo).save(any(KClientDetails.class));
    verify(passwordEncoder).encode(clientDetails.getClientSecret());
  }

  @Test
  public void shouldRemoveClientDetailsWithValidClientId() {
    // Given
    final String clientId = string().next();

    // And
    given(clientDetailsRepo.deleteByClientId(clientId)).willReturn(true);

    // When
    clientDetailsService.removeClientDetails(clientId);
  }

  @Test(expected = NoSuchClientException.class)
  public void shouldThrowsExceptionWhenTryToRemoveClientDetailsWithInvalidClientId() {
    // Given
    final String clientId = string().next();

    // And
    given(clientDetailsRepo.deleteByClientId(clientId)).willReturn(false);

    // When
    clientDetailsService.removeClientDetails(clientId);
  }

  @Test
  public void shouldUpdateClientDetails() throws NoSuchClientException {
    // Given
    final ClientDetails clientDetails = ClientDetailsBuilder.clientDetailsBuilder().build();

    // And
    given(clientDetailsRepo.update(any(KClientDetails.class))).willReturn(true);

    // When
    clientDetailsService.updateClientDetails(clientDetails);
  }

  @Test(expected = NoSuchClientException.class)
  public void shouldNotUpdateClientDetailsWhenClientIdIsNotValid() throws NoSuchClientException {
    // Given
    final ClientDetails clientDetails = ClientDetailsBuilder.clientDetailsBuilder().build();

    // And
    given(clientDetailsRepo.update(any(KClientDetails.class))).willReturn(false);

    // When
    clientDetailsService.updateClientDetails(clientDetails);
  }

  @Test
  public void shouldUpdateClientSecret() throws NoSuchClientException {
    // Given
    final String clientId = string().next();
    final String secret = string().next();

    // And
    final String expectedNewSecret = string().next();
    given(passwordEncoder.encode(secret)).willReturn(expectedNewSecret);

    // And
    given(clientDetailsRepo.updateClientSecret(clientId, expectedNewSecret)).willReturn(true);

    // When
    clientDetailsService.updateClientSecret(clientId, secret);
  }

  @Test(expected = NoSuchClientException.class)
  public void shouldNotUpdateClientSecretWhenClientIdIsInvalid() throws NoSuchClientException {
    // Given
    final String clientId = string().next();
    final String secret = string().next();

    // And
    final String expectedNewSecret = string().next();
    given(passwordEncoder.encode(secret)).willReturn(expectedNewSecret);

    // And
    given(clientDetailsRepo.updateClientSecret(clientId, expectedNewSecret)).willReturn(false);

    // When
    clientDetailsService.updateClientSecret(clientId, secret);
  }

  @Test
  public void shouldLoadClientByClientId() throws NoSuchClientException {
    // Given
    final String clientId = string().next();

    final KClientDetails expectedClientDetails =
        KClientDetailsBuilder.kClientDetailsBuilder().build();
    given(clientDetailsRepo.findByClientId(clientId)).willReturn(expectedClientDetails);

    // When
    final ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);

    // Then
    assertThat(clientDetails.getClientId()).isEqualTo(expectedClientDetails.getClientId());
  }
}
