package services;

import javax.inject.Inject;
import org.abstractj.kalium.keys.AuthenticationKey;
import org.mindrot.jbcrypt.BCrypt;
import play.Configuration;
import play.Logger;

public class AuthenticationService {

  private final static int bcryptWorkFactor = 16;
  private final AuthenticationKey authenticationKey;

  @Inject
  public AuthenticationService(Configuration configuration) {
    String cryptoSecret = configuration.getString("play.crypto.secret");
    authenticationKey = new AuthenticationKey(cryptoSecret.getBytes());
    Logger.info("info");
  }

  public String derp() {
    return "derp";
  }

  /**
   * Generate a salt.
   *
   * @return The generated salt.
   */
  public static String generateSalt() {
    return BCrypt.gensalt(bcryptWorkFactor);
  }

  /**
   * Hash a password with BCrypt.
   *
   * @param plaintextPassword The plaintext password to hash.
   * @param salt              The salt to use during hashing.
   * @return The hashed password.
   */
  public static String hashPassword(String plaintextPassword, String salt) {
    return BCrypt.hashpw(plaintextPassword, salt);
  }

  public static boolean checkPassword(String password, String hash) {
    return BCrypt.checkpw(password, hash);
  }
}
