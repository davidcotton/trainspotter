package services;

import org.mindrot.jbcrypt.BCrypt;

public class AuthenticationService {

  private static final int bcryptWorkFactor = 16;

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
   * @param plaintext The plaintext password to hash.
   * @param salt      The salt to use during hashing.
   * @return The hashed password.
   */
  public static String hashPassword(String plaintext, String salt) {
    return BCrypt.hashpw(plaintext, salt);
  }

  /**
   * Check that a plaintext password matches the hashed password.
   *
   * @param plaintext The plaintext password to verify.
   * @param hash      The previously hashed password.
   * @return          true if the passwords match, false otherwise.
   */
  boolean checkPassword(String plaintext, String hash) {
    return BCrypt.checkpw(plaintext, hash);
  }
}
