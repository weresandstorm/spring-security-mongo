package me.konglong.springsecurity.builders;

import me.konglong.springsecurity.domain.KRefreshToken;
import uk.org.fyodor.generators.RDG;

public class KRefreshTokenBuilder {

  private String tokenId = RDG.string().next();
  private byte[] token = RDG.string().next().getBytes();
  private byte[] authentication = RDG.string().next().getBytes();

  private KRefreshTokenBuilder() {}

  public static KRefreshTokenBuilder kRefreshTokenBuilder() {
    return new KRefreshTokenBuilder();
  }

  public KRefreshToken build() {
    return new KRefreshToken(tokenId, token, authentication);
  }

  public KRefreshTokenBuilder token(final byte[] oAuth2RefreshTokenSer) {
    this.token = oAuth2RefreshTokenSer;
    return this;
  }

  public KRefreshTokenBuilder authentication(final byte[] authenticationSer) {
    this.authentication = authenticationSer;
    return this;
  }
}
