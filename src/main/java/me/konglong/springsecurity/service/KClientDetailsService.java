package me.konglong.springsecurity.service;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import me.konglong.springsecurity.domain.ClientDetailsRepo;
import me.konglong.springsecurity.domain.KClientDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.ClientRegistrationService;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.stereotype.Component;

@Component
public class KClientDetailsService implements ClientDetailsService, ClientRegistrationService {

  private final ClientDetailsRepo clientDetailsRepo;

  private final PasswordEncoder passwordEncoder;

  public KClientDetailsService(
      final ClientDetailsRepo clientDetailsRepo, final PasswordEncoder passwordEncoder) {
    this.clientDetailsRepo = clientDetailsRepo;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public ClientDetails loadClientByClientId(final String clientId) {
    try {
      return clientDetailsRepo.findByClientId(clientId);
    } catch (IllegalArgumentException e) {
      throw new ClientRegistrationException("No Client Details for client id", e);
    }
  }

  @Override
  public void addClientDetails(final ClientDetails clientDetails) {
    final KClientDetails concreteClientDetails =
        new KClientDetails(
            clientDetails.getClientId(),
            passwordEncoder.encode(clientDetails.getClientSecret()),
            clientDetails.getScope(),
            clientDetails.getResourceIds(),
            clientDetails.getAuthorizedGrantTypes(),
            clientDetails.getRegisteredRedirectUri(),
            newArrayList(clientDetails.getAuthorities()),
            clientDetails.getAccessTokenValiditySeconds(),
            clientDetails.getRefreshTokenValiditySeconds(),
            clientDetails.getAdditionalInformation(),
            getAutoApproveScopes(clientDetails));

    clientDetailsRepo.save(concreteClientDetails);
  }

  @Override
  public void updateClientDetails(final ClientDetails clientDetails) {
    final KClientDetails concreteClientDetails =
        new KClientDetails(
            clientDetails.getClientId(),
            clientDetails.getClientSecret(),
            clientDetails.getScope(),
            clientDetails.getResourceIds(),
            clientDetails.getAuthorizedGrantTypes(),
            clientDetails.getRegisteredRedirectUri(),
            newArrayList(clientDetails.getAuthorities()),
            clientDetails.getAccessTokenValiditySeconds(),
            clientDetails.getRefreshTokenValiditySeconds(),
            clientDetails.getAdditionalInformation(),
            getAutoApproveScopes(clientDetails));
    final boolean result = clientDetailsRepo.update(concreteClientDetails);

    if (!result) {
      throw new NoSuchClientException("No such Client Id");
    }
  }

  @Override
  public void updateClientSecret(final String clientId, final String secret) {
    final boolean result =
        clientDetailsRepo.updateClientSecret(clientId, passwordEncoder.encode(secret));
    if (!result) {
      throw new NoSuchClientException("No such client id");
    }
  }

  @Override
  public void removeClientDetails(String clientId) {
    final boolean result = clientDetailsRepo.deleteByClientId(clientId);
    if (!result) {
      throw new NoSuchClientException("No such client id");
    }
  }

  @Override
  public List<ClientDetails> listClientDetails() {
    final List<KClientDetails> allClientDetails = clientDetailsRepo.findAll();
    return newArrayList(allClientDetails);
  }

  private Set<String> getAutoApproveScopes(final ClientDetails clientDetails) {
    if (clientDetails.isAutoApprove("true")) {
      return newHashSet("true"); // all scopes autoapproved
    }

    return clientDetails
        .getScope()
        .stream()
        .filter(clientDetails::isAutoApprove)
        .collect(Collectors.toSet());
  }
}
