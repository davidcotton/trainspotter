package validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import play.data.validation.Constraints.Validator;
import play.libs.F;
import validators.CustomConstraints.Password;

public class PasswordValidator extends Validator<String>
    implements ConstraintValidator<Password, String> {

  public static final String message = "Your password is not secure enough. "
      + "It needs to be a minimum of eight characters long and contain at least one number, "
      + "letter and special character.";
  private static final int minLength = 8;
  private static final int maxLength = 72; // BCrypt will silently truncate after 72 chars
  private static final String regex = "^(?=.*[\\d])(?=.*[a-zA-Z])(?=.*[\\W])(?=\\S+$)"
      + ".{" + minLength + "," + maxLength + "}$";
  private static final Pattern pattern = Pattern.compile(regex);

  /**
   * Initializes the validator.
   *
   * @param constraintAnnotation annotation instance for a given constraint declaration
   */
  @Override
  public void initialize(Password constraintAnnotation) {}

  /**
   * Check that a password is valid.
   */
  @Override
  public boolean isValid(String password) {
    if (password == null) {
      return false;
    }

    Matcher matcher = pattern.matcher(password);

    return matcher.find();
  }

  /**
   * Fetch the error message.
   *
   * @return A Tuple with the error message.
   */
  @Override
  public F.Tuple<String, Object[]> getErrorMessageKey() {
    return F.Tuple(message, new Object[]{});
  }
}
