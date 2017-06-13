package validators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PasswordValidatorTest {

  private PasswordValidator passwordValidator = new PasswordValidator();

  @Test public void isValid_success() throws Exception {
    assertTrue(passwordValidator.isValid("password1!"));
    assertTrue(passwordValidator.isValid("!password1"));
    assertTrue(passwordValidator.isValid("1password!"));
    assertTrue(passwordValidator.isValid("!1password"));
  }

  @Test public void isValid_failureGivenTooShort() throws Exception {
    assertFalse(passwordValidator.isValid("pass1!"));
  }

  @Test public void isValid_failureGivenTooLong() throws Exception {
    // 73 characters long string
    assertFalse(passwordValidator.isValid("hlfqzefgwcoxcytcnxiaftxkrldoeocpfpsyonbmtlomkhymhiqvtzzgvmnfzsirvqqxdnrbw"));
  }

  @Test public void isValid_failureGivenNoSpecialCharacter() throws Exception {
    assertFalse(passwordValidator.isValid("password1"));
  }

  @Test public void isValid_failureGivenNoNumber() throws Exception {
    assertFalse(passwordValidator.isValid("password!"));
  }

  @Test public void isValid_failureGivenNoLetter() throws Exception {
    assertFalse(passwordValidator.isValid("1234567!"));
  }

  @Test public void isValid_failureGivenLeadingWhitespace() throws Exception {
    assertFalse(passwordValidator.isValid(" password1!"));
  }

  @Test public void isValid_failureGivenTrailingWhitespace() throws Exception {
    assertFalse(passwordValidator.isValid("password1! "));
  }
}
