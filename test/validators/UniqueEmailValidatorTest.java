package validators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import models.User;

import repositories.UserRepository;

@RunWith(MockitoJUnitRunner.class)
public class UniqueEmailValidatorTest {

  @InjectMocks
  private UniqueEmailValidator uniqueEmailValidator;

  @Mock
  private UserRepository mockUserRepository;

  @Test
  public void isValid_givenEmailNotInDb() {
    // ARRANGE
    String email = "john.digweed@bedrock.com";
    when(mockUserRepository.findByEmail(email)).thenReturn(Optional.empty());

    // ACT
    boolean result = uniqueEmailValidator.isValid(email);

    // ASSERT
    assertTrue(result);
  }

  @Test
  public void isValid_givenEmailInDb() {
    // ARRANGE
    String email = "john.digweed@bedrock.com";
    when(mockUserRepository.findByEmail(email)).thenReturn(Optional.of(mock(User.class)));

    // ACT
    boolean result = uniqueEmailValidator.isValid(email);

    // ASSERT
    assertFalse(result);
  }
}
