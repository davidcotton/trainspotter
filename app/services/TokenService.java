package services;

import static java.util.Objects.requireNonNull;
import static models.Token.TOKEN_EXPIRY_PERIOD_HOURS;

import java.time.ZonedDateTime;
import javax.inject.Inject;
import models.Token;
import models.User;
import org.abstractj.kalium.keys.AuthenticationKey;
import play.Logger;
import repositories.TokenRepository;

public class TokenService {

  private final TokenRepository tokenRepository;
  private final AuthenticationKey authenticationKey;

  @Inject
  public TokenService(TokenRepository tokenRepository, AuthenticationKey authenticationKey) {
    this.tokenRepository = requireNonNull(tokenRepository);
    this.authenticationKey = requireNonNull(authenticationKey);
  }

  /**
   * Create or update an authentication token.
   *
   * Only a single auth token can exist for a user,
   *   so if one already exists update it with a new expiry time.
   *
   * @param user The user to create the token for.
   * @return An authentication token.
   */
  public Token create(User user) {
    ZonedDateTime expiry = ZonedDateTime.now().plusHours(TOKEN_EXPIRY_PERIOD_HOURS);
    byte[] signature = authenticationKey.sign(
        String.format("%s:%s", user.getEmail(), expiry.toString()).getBytes()
    );
    String derp = new String(signature);
    Logger.info(derp);

    return tokenRepository.findByUser(user)
        .map(token -> {
          token.setSignature(signature);
          token.setExpiry(expiry);
          tokenRepository.update(token);
          return token;
        })
        .orElseGet(() -> {
          Token token = new Token(user, signature, expiry);
          tokenRepository.insert(token);
          return token;
        });
  }

  /**
   * Does a user have a valid login token?
   *
   * @param userId    The user to check.
   * @param signature The authentication token.
   * @return True if the user is authorised else false.
   */
  public boolean isAuthorised(long userId, String signature) {
    return tokenRepository
        .findByUserId(userId)
        .map(token -> token.isValid(signature.getBytes(), signature))
        .orElse(false);
  }
}
