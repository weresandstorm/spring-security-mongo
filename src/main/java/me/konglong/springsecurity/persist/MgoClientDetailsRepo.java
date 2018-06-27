/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package me.konglong.springsecurity.persist;

import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

import me.konglong.springsecurity.domain.ClientDetailsRepo;
import me.konglong.springsecurity.domain.KClientDetails;
import org.springframework.stereotype.Component;

@Component
public class MgoClientDetailsRepo extends MgoRepoBase<KClientDetails, String>
    implements ClientDetailsRepo {

  @Override
  public String collectionName() {
    return "client_details";
  }

  @Override
  public boolean deleteByClientId(String clientId) {
    return delete(clientId);
  }

  @Override
  public boolean update(KClientDetails newDetails) {
    return update(
        newDetails.id(),
        combine(
            set("scope", newDetails.getScope()),
            set("resourceIds", newDetails.getResourceIds()),
            set("authorizedGrantTypes", newDetails.getAuthorizedGrantTypes()),
            set("authorities", newDetails.getAuthorities()),
            set("accessTokenValiditySeconds", newDetails.getAccessTokenValiditySeconds()),
            set("refreshTokenValiditySeconds", newDetails.getRefreshTokenValiditySeconds()),
            set("additionalInfo", newDetails.getAdditionalInformation()),
            set("autoApproveScopes", newDetails.getAutoApproveScopes()),
            set("registeredRedirectUris", newDetails.getRegisteredRedirectUri())));
  }

  @Override
  public boolean updateClientSecret(String clientId, String newSecret) {
    return update(clientId, set("clientSecret", newSecret));
  }

  @Override
  public KClientDetails findByClientId(String clientId) {
    return get(clientId);
  }
}
