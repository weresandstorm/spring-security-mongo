package me.konglong.springsecurity.domain;

import java.util.List;

public interface AccessTokenRepo extends Repo<KAccessToken, String> {

    KAccessToken findByTokenId(String tokenId);

    boolean deleteByTokenId(String tokenId);

    boolean deleteByRefreshTokenId(String refreshTokenId);

    KAccessToken findByAuthenticationId(String key);

    List<KAccessToken> findByUsernameAndClientId(String username, String clientId);

    List<KAccessToken> findByClientId(String clientId);

}
