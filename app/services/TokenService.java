package services;

import static java.util.Objects.requireNonNull;
import static models.Token.TOKEN_EXPIRY_PERIOD_HOURS;

import com.google.common.io.BaseEncoding;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;
import models.Token;
import models.User;
import play.Configuration;
import repositories.TokenRepository;

public class TokenService {

  private static final String HMAC_ALGORITHM = "HmacSHA256";
  private final TokenRepository tokenRepository;
  private final Mac mac;

  @Inject
  public TokenService(TokenRepository tokenRepository, Configuration configuration)
      throws InvalidKeyException, NoSuchAlgorithmException {
    this.tokenRepository = requireNonNull(tokenRepository);
    String secretKey = configuration.getString("play.crypto.secret");
    if (secretKey.length() != 32) {
      throw new InvalidKeyException("Must set a 32 character crypto secret");
    }
    SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(), HMAC_ALGORITHM);
    mac = Mac.getInstance(HMAC_ALGORITHM);
    mac.init(signingKey);
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
  public Token generate(User user) {
    ZonedDateTime expiry = ZonedDateTime
        .now()
        .truncatedTo(ChronoUnit.SECONDS)
        .plusHours(TOKEN_EXPIRY_PERIOD_HOURS);
    String signature = sign(user, expiry);

    return tokenRepository.findByUser(user)
        .map(token -> {
          token.update(expiry, signature);
          tokenRepository.update(token);
          return token;
        })
        .orElseGet(() -> {
          Token token = new Token(user, expiry, signature);
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
  public boolean isAuthorised(long userId, ZonedDateTime expiry, String signature) {
    return tokenRepository
        .findByUserId(userId)
        .map(token -> token.isValid(expiry, signature))
        .orElse(false);
  }

  /**
   * Generate a token signature.
   *
   * @param user    The user to generate the token for.
   * @param expiry  The token expiration time.
   * @return A token signature.
   */
  private String sign(User user, ZonedDateTime expiry) {
    String signature = String.format("%s:%s", user.getId(), expiry.toString());
    byte[] bytes = mac.doFinal(signature.getBytes());
    return BaseEncoding.base64().encode(bytes);
  }
}
