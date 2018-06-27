package me.konglong.springsecurity.service;

import java.util.UUID;
import me.konglong.springsecurity.domain.KClientToken;
import me.konglong.springsecurity.domain.ClientTokenRepo;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.ClientKeyGenerator;
import org.springframework.security.oauth2.client.token.ClientTokenServices;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import org.springframework.stereotype.Component;

@Component
public class KClientTokenService implements ClientTokenServices {

  private final ClientTokenRepo clientTokenRepo;

  private final ClientKeyGenerator clientKeyGenerator;

  public KClientTokenService(
      final ClientTokenRepo clientTokenRepo, final ClientKeyGenerator clientKeyGenerator) {
    this.clientTokenRepo = clientTokenRepo;
    this.clientKeyGenerator = clientKeyGenerator;
  }

  @Override
  public OAuth2AccessToken getAccessToken(
      final OAuth2ProtectedResourceDetails resource, final Authentication authentication) {
    final KClientToken ClientToken =
        clientTokenRepo.findByAuthenticationId(
            clientKeyGenerator.extractKey(resource, authentication));
    return SerializationUtils.deserialize(ClientToken.getToken());
  }

  @Override
  public void saveAccessToken(
      final OAuth2ProtectedResourceDetails resource,
      final Authentication authentication,
      final OAuth2AccessToken accessToken) {
    removeAccessToken(resource, authentication);
    final KClientToken ClientToken =
        new KClientToken(
            UUID.randomUUID().toString(),
            accessToken.getValue(),
            SerializationUtils.serialize(accessToken),
            clientKeyGenerator.extractKey(resource, authentication),
            authentication.getName(),
            resource.getClientId());

    clientTokenRepo.save(ClientToken);
  }

  @Override
  public void removeAccessToken(
      final OAuth2ProtectedResourceDetails resource, final Authentication authentication) {
    clientTokenRepo.deleteByAuthenticationId(
        clientKeyGenerator.extractKey(resource, authentication));
  }
}
