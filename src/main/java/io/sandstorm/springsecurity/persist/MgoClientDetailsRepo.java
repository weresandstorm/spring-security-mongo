/*
 * Copyright (C) 2018 The Sandstorm Org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.sandstorm.springsecurity.persist;

import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

import io.sandstorm.springsecurity.domain.ClientDetailsRepo;
import io.sandstorm.springsecurity.domain.ConcreteClientDetails;
import org.springframework.stereotype.Component;

@Component
public class MgoClientDetailsRepo extends MgoRepoBase<ConcreteClientDetails, String>
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
  public boolean update(ConcreteClientDetails newDetails) {
    return update(
        newDetails.id(),
        combine(
            set("scope", newDetails.getScope()),
            set("resourceIds", newDetails.getResourceIds()),
            set("authorizedGrantTypes", newDetails.getAuthorizedGrantTypes()),
            set("grantedAuthorities", newDetails.getAuthorities()),
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
  public ConcreteClientDetails findByClientId(String clientId) {
    return get(clientId);
  }
}
