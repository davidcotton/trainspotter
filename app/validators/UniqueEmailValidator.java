package validators;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import models.User;
import play.data.validation.Constraints;
import play.libs.F;
import repositories.UserRepository;
import validators.CustomConstraints.UniqueEmail;

/**
 * Ensure that an user's email does not exist in the DB.
 */
public class UniqueEmailValidator extends Constraints.Validator<String>
    implements ConstraintValidator<UniqueEmail, String> {

  public static final String message = "This email address is already registered.";

  private final UserRepository userRepository;

  @Inject
  public UniqueEmailValidator(UserRepository userRepository) {
    this.userRepository = requireNonNull(userRepository);
  }

  /**
   * Initializes the validator.
   *
   * @param constraintAnnotation annotation instance for a given constraint declaration
   */
  @Override
  public void initialize(UniqueEmail constraintAnnotation) {}

  /**
   * Check that an email address does not exist in the db.
   *
   * @param email The email address to search for.
   * @return      Whether the email has not been registered.
   */
  @Override
  public boolean isValid(String email) {
    Optional<User> maybeUser = userRepository.findByEmail(email);
    return !maybeUser.isPresent();
  }

  /**
   * Fetch the error message.
   *
   * @return A Tuple with the error message.
   */
  @Override
  public F.Tuple<String, Object[]> getErrorMessageKey() {
    return F.Tuple(message, new Object[] {});
  }
}
