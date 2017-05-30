package services;

import org.mindrot.jbcrypt.BCrypt;

public class AuthenticationService {

  private final static int bcryptWorkFactor = 16;

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
