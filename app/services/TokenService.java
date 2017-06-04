package services;

import static java.util.Objects.requireNonNull;

import java.time.ZonedDateTime;
import javax.inject.Inject;
import models.Token;
import models.User;
import org.abstractj.kalium.keys.AuthenticationKey;
import play.Configuration;
import repositories.TokenRepository;

public class TokenService {

  private final TokenRepository tokenRepository;
  private final AuthenticationKey authenticationKey;

  @Inject
  public TokenService(Configuration configuration, TokenRepository tokenRepository) {
    this.tokenRepository = requireNonNull(tokenRepository);
    authenticationKey = new AuthenticationKey(
        configuration.getString("play.crypto.secret").getBytes()
    );
  }

  /**
   * Create a new authentication token.
   *
   * @param user  The user to create the token for.
   * @return An authentication token.
   */
  public Token create(User user) {
    ZonedDateTime expiry = ZonedDateTime.now().plusHours(4);
    String raw = String.format("%s:%s", user.getEmail(), expiry.toString());
    byte[] hmac = authenticationKey.sign(raw.getBytes());
    Token token = new Token(user, hmac, expiry);
    tokenRepository.insert(token);

    return token;
  }
}
