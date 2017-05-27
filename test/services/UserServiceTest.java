package services;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JsonNode;
import io.atlassian.fugue.Either;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import models.User;

import org.hamcrest.collection.IsEmptyCollection;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import play.data.Form;
import play.data.FormFactory;

import play.data.validation.ValidationError;
import repositories.UserRepository;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

  @InjectMocks
  private UserService userService;

  @Mock
  private UserRepository mockUserRepository;

  @Mock
  private FormFactory mockFormFactory;

  @Test
  public void fetchAll() {
    // ARRANGE
    when(mockUserRepository.findAll()).thenReturn(new ArrayList<User>() {{
      add(mock(User.class));
      add(mock(User.class));
    }});

    // ACT
    List<User> actualUsers = userService.fetchAll();

    // ASSERT
    assertThat(actualUsers, not(IsEmptyCollection.empty()));
    assertThat(actualUsers.size(), is(2));
  }

  @Test
  public void findById_givenIdInDb() {
    // ARRANGE
    long id = 1L;
    when(mockUserRepository.findById(id)).thenReturn(Optional.of(mock(User.class)));

    // ACT
    Optional<User> maybeUser = userService.findById(id);

    // ASSERT
    assertTrue(maybeUser.isPresent());
  }

  @Test
  public void findById_givenIdNotInDb() {
    // ARRANGE
    long nonExistentId = 1L;
    when(mockUserRepository.findById(nonExistentId)).thenReturn(Optional.empty());

    // ACT
    Optional<User> maybeUser = userService.findById(nonExistentId);

    // ASSERT
    assertThat(maybeUser.isPresent(), is(false));
  }

  @Test
  public void findByEmail_givenEmailInDb() {
    // ARRANGE
    String email = "max.power@simpsons.com";
    when(mockUserRepository.findByEmail(email)).thenReturn(Optional.of(mock(User.class)));

    // ACT
    Optional<User> maybeUser = userService.findByEmail(email);

    // ASSERT
    assertTrue(maybeUser.isPresent());
  }

  @Test
  public void findByEmail_givenEmailNotInDb() {
    // ARRANGE
    String email = "joey.jo-jo.junior.shabadoo@simpsons.com";
    when(mockUserRepository.findByEmail(email)).thenReturn(Optional.empty());

    // ACT
    Optional<User> maybeUser = userService.findByEmail(email);

    // ASSERT
    assertThat(maybeUser.isPresent(), is(false));
  }

  @Test
  public void insert_successGivenValidData() {
    // ARRANGE
    String email = "guy.incognito@simpsons.com";
    User user = new User(null, email, "password", null, "displayName", null, null, null, null);

    Form mockForm = mock(Form.class);
    Form mockDataForm = mock(Form.class);

    when(mockFormFactory.form(User.class, User.InsertValidators.class)).thenReturn(mockDataForm);
    when(mockDataForm.bind(any(JsonNode.class))).thenReturn(mockForm);
    when(mockForm.hasErrors()).thenReturn(false);

    // ACT
    Either<Map<String, List<ValidationError>>, User> userOrError = userService.insert(user);

    // ASSERT
    // assert left (error value) is not present
    assertFalse(userOrError.isLeft());
    // assert right (success value) is present
    assertTrue(userOrError.isRight());
    assertThat(userOrError.right().get(), instanceOf(User.class));
    // verify that the user repository inserted the new user
    ArgumentCaptor<User> argument = ArgumentCaptor.forClass(User.class);
    verify(mockUserRepository).insert(argument.capture());
    assertThat(argument.getValue().getEmail(), is(email));
  }

  @Test
  public void insert_failureGivenInvalidData() {
    // ARRANGE
    User user = new User(null, "invalid email format", null, null, null, null, null, null, null);

    Map<String, List<ValidationError>> validationErrors =
        new HashMap<String, List<ValidationError>>() {{
          put("email", mock(List.class));
        }};

    Form mockForm = mock(Form.class);
    Form mockDataForm = mock(Form.class);

    when(mockFormFactory.form(User.class, User.InsertValidators.class)).thenReturn(mockDataForm);
    when(mockDataForm.bind(any(JsonNode.class))).thenReturn(mockForm);
    when(mockForm.hasErrors()).thenReturn(true);
    when(mockForm.errors()).thenReturn(validationErrors);

    // ACT
    Either<Map<String, List<ValidationError>>, User> userOrError = userService.insert(user);

    // ASSERT
    // assert right (success value) is not present
    assertFalse(userOrError.isRight());
    // assert left (error value) is present
    assertTrue(userOrError.isLeft());
    assertThat(userOrError.left().get().get("email"), instanceOf(List.class));
    // verify that the userRepository never tried to insert the invalid user
    verify(mockUserRepository, never()).insert(any());
  }

}
