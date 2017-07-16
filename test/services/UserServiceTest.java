package services;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import io.atlassian.fugue.Either;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import models.User;
import models.User.Status;
import models.create.CreateUser;
import models.LoginUser;
import models.Token;
import models.update.UpdateUser;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import play.data.Form;
import play.data.validation.ValidationError;
import repositories.UserRepository;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

  @InjectMocks private UserService userService;
  @Mock private UserRepository mockUserRepository;
  @Mock private TokenService mockTokenService;
  @Mock private Form<CreateUser> mockCreateUserForm;
  @Mock private Form<UpdateUser> mockUpdateUserForm;

  @Test public void fetchAll() {
    // ARRANGE
    when(mockUserRepository.findAllActiveUsers()).thenReturn(new ArrayList<User>() {{
      add(mock(User.class));
      add(mock(User.class));
    }});

    // ACT
    List<User> actualUsers = userService.fetchAll();

    // ASSERT
    assertThat(actualUsers, not(IsEmptyCollection.empty()));
    assertThat(actualUsers.size(), is(2));
  }

  @Test public void findById_whenIdInDb() {
    // ARRANGE
    long id = 1L;
    when(mockUserRepository.findActiveById(id)).thenReturn(Optional.of(mock(User.class)));

    // ACT
    Optional<User> maybeUser = userService.findActiveById(id);

    // ASSERT
    assertTrue(maybeUser.isPresent());
  }

  @Test public void findById_whenIdNotInDb() {
    // ARRANGE
    long nonExistentId = 1L;
    when(mockUserRepository.findActiveById(nonExistentId)).thenReturn(Optional.empty());

    // ACT
    Optional<User> maybeUser = userService.findActiveById(nonExistentId);

    // ASSERT
    assertThat(maybeUser.isPresent(), is(false));
  }

  @Test public void findByEmail_whenEmailInDb() {
    // ARRANGE
    String email = "max.power@simpsons.com";
    when(mockUserRepository.findByEmail(email)).thenReturn(Optional.of(mock(User.class)));

    // ACT
    Optional<User> maybeUser = userService.findByEmail(email);

    // ASSERT
    assertTrue(maybeUser.isPresent());
  }

  @Test public void findByEmail_whenEmailNotInDb() {
    // ARRANGE
    String email = "joey.jo-jo.junior.shabadoo@simpsons.com";
    when(mockUserRepository.findByEmail(email)).thenReturn(Optional.empty());

    // ACT
    Optional<User> maybeUser = userService.findByEmail(email);

    // ASSERT
    assertThat(maybeUser.isPresent(), is(false));
  }

  @Test public void insert_success() {
    // ARRANGE
    CreateUser createUser = new CreateUser("brian.mcgee@simpsons.com", "Brian McGee", "password1!");

    when(mockCreateUserForm.hasErrors()).thenReturn(false);
    when(mockCreateUserForm.get()).thenReturn(createUser);

    // ACT
    Either<Form<CreateUser>, User> userOrError = userService.insert(mockCreateUserForm);

    // ASSERT
    // assert left (error value) is not present
    assertFalse(userOrError.isLeft());
    // assert right (success value) is present
    assertTrue(userOrError.isRight());
    assertThat(userOrError.right().get(), instanceOf(User.class));
    // verify that the user repository inserted the new user
    ArgumentCaptor<User> argument = ArgumentCaptor.forClass(User.class);
    verify(mockUserRepository).insert(argument.capture());
    assertThat(argument.getValue().getEmail(), is("brian.mcgee@simpsons.com"));
  }

  @Test public void insert_failureWhenFailsValidation() {
    // ARRANGE
    when(mockCreateUserForm.hasErrors()).thenReturn(true);
    User mockUser = mock(User.class);

    // ACT
    Either<Form<CreateUser>, User> userOrError = userService.insert(mockCreateUserForm);

    // ASSERT
    // assert right (success value) is not present
    assertFalse(userOrError.isRight());
    // assert left (error value) is present
    assertTrue(userOrError.isLeft());
    // verify that the userRepository never tried to insert the invalid user
    verify(mockUserRepository, never()).insert(mockUser);
  }

  @Test public void update_success() {
    // ARRANGE
    User savedUser = new User(
        1L, "brian.mcgee@simpsons.com", "Brian McGee", "brian-mcgee",
        Status.active, 0, 0, 0, "", null,
        null, null, null, ZonedDateTime.now(), ZonedDateTime.now()
    );
    UpdateUser updateUser = new UpdateUser("joey-jo-jo-junior-shabadoo@simpsons.com", "Joey Jo-jo Junior Shabadoo");

    when(mockUpdateUserForm.hasErrors()).thenReturn(false);
    when(mockUpdateUserForm.get()).thenReturn(updateUser);

    // ACT
    Either<Form<UpdateUser>, User> userOrError = userService.updateUser(mockUpdateUserForm, savedUser);

    // ASSERT
    // assert left (error value) is not present
    assertFalse(userOrError.isLeft());
    // assert right (success value) is present
    assertTrue(userOrError.isRight());
    // verify that the user repository updated the user
    ArgumentCaptor<User> argument = ArgumentCaptor.forClass(User.class);
    verify(mockUserRepository).update(argument.capture());
    assertThat(argument.getValue().getEmail(), is("joey-jo-jo-junior-shabadoo@simpsons.com"));
  }

  @Test public void update_failureWhenFailsValidation() {
    // ARRANGE
    User savedUser = new User(
        1L, "brian.mcgee@simpsons.com", "Brian McGee", "brian-mcgee",
        Status.active, 0, 0, 0, "", null,
        null, null, null, ZonedDateTime.now(), ZonedDateTime.now()
    );
    User mockUser = mock(User.class);

    when(mockUpdateUserForm.hasErrors()).thenReturn(true);

    // ACT
    Either<Form<UpdateUser>, User> userOrError = userService.updateUser(mockUpdateUserForm, savedUser);

    // ASSERT
    // assert right (success value) is not present
    assertFalse(userOrError.isRight());
    // assert left (error value) is present
    assertTrue(userOrError.isLeft());
    // verify that the userRepository never tried to insert the invalid user
    verify(mockUserRepository, never()).update(mockUser);
  }

  @Test public void loginToken_successWhenValidDetailsProvided() {
    // ARRANGE
    String email = "brian.mcgee@simpsons.com";
    String password = "password1!";
    LoginUser loginUser = new LoginUser(email, password);
    User mockUser = mock(User.class);
    Token token = new Token(mockUser, ZonedDateTime.now(), "signature");

    when(mockUserRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));
    when(mockUser.isAuthorised(password)).thenReturn(true);
    when(mockTokenService.generate(mockUser)).thenReturn(token);

    // ACT
    Either<Map<String, List<ValidationError>>, Token> tokenOrError = userService.loginToken(loginUser);

    // ASSERT
    // assert left (error value) is not present
    assertFalse(tokenOrError.isLeft());
    // assert right (success value) is present
    assertTrue(tokenOrError.isRight());
    // verify that the userRepository searched for the user
    verify(mockUserRepository).findByEmail(email);
    // verify we checked the password
    verify(mockUser).isAuthorised(password);
    // verify that the tokenService created a new auth token
    verify(mockTokenService).generate(mockUser);
  }

  @Test public void loginToken_failureWhenEmailNotFound() {
    // ARRANGE
    String email = "not.in.database@example.com";
    String password = "password1!";
    LoginUser loginUser = new LoginUser(email, password);
    Optional<User> emptyUser = Optional.empty();

    // user not found
    when(mockUserRepository.findByEmail(email)).thenReturn(emptyUser);

    // ACT
    Either<Map<String, List<ValidationError>>, Token> tokenOrError = userService.loginToken(loginUser);

    // ASSERT
    // assert right (success value) is not present
    assertFalse(tokenOrError.isRight());
    // assert left (error value) is present
    assertTrue(tokenOrError.isLeft());
    // verify that the userRepository searched for the user and it was empty
    verify(mockUserRepository).findByEmail(loginUser.getEmail());
    assertEquals(Optional.empty(), emptyUser);
    // verify we didn't try to fetch a token
    verifyZeroInteractions(mockTokenService);
  }

  @Test public void login_failureGivenWrongPassword() {
    // ARRANGE
    String email = "brian.mcgee@simpsons.com";
    String password = "wrong password";
    LoginUser loginUser = new LoginUser(email, password);
    User mockUser = mock(User.class);

    when(mockUserRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));
    // invalid password
    when(mockUser.isAuthorised(password)).thenReturn(false);

    // ACT
    Either<Map<String, List<ValidationError>>, Token> tokenOrError = userService.loginToken(loginUser);

    // ASSERT
    // assert right (success value) is not present
    assertFalse(tokenOrError.isRight());
    // assert left (error value) is present
    assertTrue(tokenOrError.isLeft());
    // verify that the userRepository searched for the user
    verify(mockUserRepository).findByEmail(email);
    // verify we checked the password
    verify(mockUser).isAuthorised(password);
    // verify we didn't try to create a token
    verifyZeroInteractions(mockTokenService);
  }
}
