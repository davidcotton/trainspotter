package services;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JsonNode;

import io.atlassian.fugue.Either;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import models.CreateUser;
import models.LoginUser;
import models.Token;
import models.User;
import models.User.Status;

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

  @InjectMocks private UserService userService;
  @Mock private UserRepository mockUserRepository;
  @Mock private FormFactory mockFormFactory;
  @Mock private TokenService mockTokenService;
  @Mock private Form mockForm;
  @Mock private Form mockDataForm;

  @Test
  public void fetchAll() {
    // ARRANGE
    when(mockUserRepository.findAllCurrentUsers()).thenReturn(new ArrayList<User>() {{
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
    CreateUser createUser = new CreateUser("john.digweed@bedrock.com", "John Digweed", "password1!");

    when(mockFormFactory.form(CreateUser.class, CreateUser.InsertValidators.class)).thenReturn(mockDataForm);
    when(mockDataForm.bind(any(JsonNode.class))).thenReturn(mockForm);
    when(mockForm.hasErrors()).thenReturn(false);

    // ACT
    Either<Map<String, List<ValidationError>>, User> userOrError = userService.insert(createUser);

    // ASSERT
    // assert left (error value) is not present
    assertFalse(userOrError.isLeft());
    // assert right (success value) is present
    assertTrue(userOrError.isRight());
    assertThat(userOrError.right().get(), instanceOf(User.class));
    // verify that the user repository inserted the new user
    ArgumentCaptor<User> argument = ArgumentCaptor.forClass(User.class);
    verify(mockUserRepository).insert(argument.capture());
    assertThat(argument.getValue().getEmail(), is("john.digweed@bedrock.com"));
  }

  @Test
  public void insert_failureGivenInvalidData() {
    // ARRANGE
    CreateUser createUser = new CreateUser("invalid email format", "MC Hammer", "password1!");

    Map<String, List<ValidationError>> validationErrors = new HashMap<String, List<ValidationError>>() {{
      put("email", mock(List.class));
    }};

    when(mockFormFactory.form(CreateUser.class, CreateUser.InsertValidators.class))
        .thenReturn(mockDataForm);
    when(mockDataForm.bind(any(JsonNode.class))).thenReturn(mockForm);
    when(mockForm.hasErrors()).thenReturn(true);
    when(mockForm.errors()).thenReturn(validationErrors);

    // ACT
    Either<Map<String, List<ValidationError>>, User> userOrError = userService.insert(createUser);

    // ASSERT
    // assert right (success value) is not present
    assertFalse(userOrError.isRight());
    // assert left (error value) is present
    assertTrue(userOrError.isLeft());
    assertThat(userOrError.left().get().get("email"), instanceOf(List.class));
    // verify that the userRepository never tried to insert the invalid user
    verify(mockUserRepository, never()).insert(any());
  }

  @Test
  public void update_successGivenValidData() {
    // ARRANGE
    User savedUser = new User(
        1L, "john.digweed@bedrock.com", "John Digweed", User.Status.active,
        "hash", "salt", new ArrayList<>(), ZonedDateTime.now(), ZonedDateTime.now()
    );
    CreateUser createUser = new CreateUser("sasha@bedrock.com", "Sasha", "password1!");

    when(mockFormFactory.form(CreateUser.class, CreateUser.UpdateValidators.class))
        .thenReturn(mockDataForm);
    when(mockDataForm.bind(any(JsonNode.class))).thenReturn(mockForm);
    when(mockForm.hasErrors()).thenReturn(false);

    // ACT
    Either<Map<String, List<ValidationError>>, User> userOrError = userService
        .update(savedUser, createUser);

    // ASSERT
    // assert left (error value) is not present
    assertFalse(userOrError.isLeft());
    // assert right (success value) is present
    assertTrue(userOrError.isRight());
    // verify that the user repository updated the user
    ArgumentCaptor<User> argument = ArgumentCaptor.forClass(User.class);
    verify(mockUserRepository).update(argument.capture());
    assertThat(argument.getValue().getEmail(), is("sasha@bedrock.com"));
    assertThat(argument.getValue().getDisplayName(), is("Sasha"));
    // make sure that password is hashed!
    assertThat(argument.getValue().getHash(), not("password1!"));
    assertNotNull(argument.getValue().getSalt());
  }

  @Test
  public void update_failureGivenInvalidEmail() {
    // ARRANGE
    User savedUser = new User(
        1L, "john.digweed@bedrock.com", "John Digweed", User.Status.active,
        "hash", "salt", new ArrayList<>(), ZonedDateTime.now(), ZonedDateTime.now()
    );
    CreateUser createUser = new CreateUser("invalid email format", "John Digweed", "password1!");

    Map<String, List<ValidationError>> validationErrors = new HashMap<String, List<ValidationError>>() {{
      put("email", mock(List.class));
    }};

    when(mockFormFactory.form(CreateUser.class, CreateUser.UpdateValidators.class)).thenReturn(mockDataForm);
    when(mockDataForm.bind(any(JsonNode.class))).thenReturn(mockForm);
    when(mockForm.hasErrors()).thenReturn(true);
    when(mockForm.errors()).thenReturn(validationErrors);

    // ACT
    Either<Map<String, List<ValidationError>>, User> userOrError = userService
        .update(savedUser, createUser);

    // ASSERT
    // assert right (success value) is not present
    assertFalse(userOrError.isRight());
    // assert left (error value) is present
    assertTrue(userOrError.isLeft());
    assertThat(userOrError.left().get().get("email"), instanceOf(List.class));
    // verify that the userRepository never tried to insert the invalid user
    verify(mockUserRepository, never()).insert(any());
  }

  @Test
  public void delete() {
    // ARRANGE
    User user = new User(
        1L, "john.digweed@bedrock.com", "John Digweed", User.Status.active,
        "hash", "salt", new ArrayList<>(), ZonedDateTime.now(), ZonedDateTime.now()
    );

    // ACT
    userService.delete(user);

    // ASSERT
    assertThat(user.getStatus(), is(Status.deleted));
    verify(mockUserRepository, times(1)).update(user);
  }

  @Test
  public void login_successGivenValidDetails() {
    // ARRANGE
    String email = "john.digweed@bedrock.com";
    String password = "password1!";
    LoginUser loginUser = new LoginUser(email, password);
    //User user = mock(User.class);
    User user = new User(
        1L, "john.digweed@bedrock.com", "John Digweed", User.Status.active,
        "hash", "salt", new ArrayList<>(), ZonedDateTime.now(), ZonedDateTime.now()
    );
    Token token = new Token(user, "value".getBytes(), ZonedDateTime.now());

    when(mockFormFactory.form(LoginUser.class, LoginUser.Validators.class)).thenReturn(mockDataForm);
    when(mockDataForm.bind(any(JsonNode.class))).thenReturn(mockForm);
    when(mockForm.hasErrors()).thenReturn(false);

    when(mockUserRepository.findByEmail(email)).thenReturn(Optional.of(user));
    //when(mockUser.getHash()).thenReturn("hashedPassword");
    when(AuthenticationService.checkPassword(password, user.getHash())).thenReturn(true);
    when(mockTokenService.create(user)).thenReturn(token);

    // ACT
    Either<Map<String, List<ValidationError>>, Token> tokenOrError = userService.login(loginUser);

    // ASSERT
    // assert left (error value) is not present
    assertFalse(tokenOrError.isLeft());
    // assert right (success value) is present
    assertTrue(tokenOrError.isRight());
    // verify that the user repository searched for the user
    verify(mockUserRepository, times(1)).findByEmail(email);
//    ArgumentCaptor<User> argument = ArgumentCaptor.forClass(User.class);
//    verify(mockUserRepository).update(argument.capture());
//    assertThat(argument.getValue().getEmail(), is("sasha@bedrock.com"));
//    assertThat(argument.getValue().getDisplayName(), is("Sasha"));

  }
}
