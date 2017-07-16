package services;

import static java.util.Objects.requireNonNull;
import static play.libs.Json.toJson;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.atlassian.fugue.Either;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.inject.Inject;
import models.User.Status;
import models.create.CreateUser;
import models.LoginUser;
import models.Token;
import models.update.UpdatePassword;
import models.update.UpdateUser;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.data.validation.ValidationError;
import repositories.UserRepository;
import views.html.user.login;

public class UserService {

  private final UserRepository userRepository;
  private final FormFactory formFactory;
  private final TokenService tokenService;
  private static final String FIELD_LOGIN = "login";
  private static final String MESSAGE_LOGIN_FAILURE = "Unable to login";

  @Inject
  public UserService(
      UserRepository userRepository,
      FormFactory formFactory,
      TokenService tokenService
  ) {
    this.userRepository = requireNonNull(userRepository);
    this.formFactory = requireNonNull(formFactory);
    this.tokenService = requireNonNull(tokenService);
  }

  /**
   * Fetch all Users.
   *
   * @return A collection of Users.
   */
  public List<User> fetchAll() {
    return userRepository.findAllActiveUsers();
  }

  /**
   * Find a User by their ID.
   *
   * @param id The ID to search for.
   * @return An optional User if found.
   */
  public Optional<User> findActiveById(long id) {
    return userRepository.findActiveById(id);
  }

  /**
   * Find a User by their email address.
   *
   * @param email The email address to search for.
   * @return An optional User if found.
   */
  public Optional<User> findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  public Optional<User> findBySlug(String slug) {
    return userRepository.findBySlug(slug);
  }

  /**
   * Create a new User in the system.
   *
   * @param userForm The user supplied data.
   * @return Either the inserted User or the form with errors.
   */
  public Either<Form<CreateUser>, User> insert(Form<CreateUser> userForm) {
    if (userForm.hasErrors()) {
      return Either.left(userForm);
    }

    User user = new User(userForm.get());
    userRepository.insert(user);

    return Either.right(user);
  }

  /**
   * Update a User.
   *
   * @param userForm  The new User data.
   * @param savedUser The existing User.
   * @return Either the updated User, or errors.
   */
  public Either<Form<UpdateUser>, User> updateUser(Form<UpdateUser> userForm, User savedUser) {
    if (userForm.hasErrors()) {
      return Either.left(userForm);
    }

    User newUser = new User(userForm.get(), savedUser);
    userRepository.update(newUser);

    return Either.right(newUser);
  }

  public Either<Form<UpdatePassword>, User> updatePassword(Form<UpdatePassword> passwordForm, User savedUser) {
    if (passwordForm.hasErrors()) {
      return Either.left(passwordForm);
    }

    User newUser = new User(passwordForm.get(), savedUser);
    userRepository.update(newUser);

    return Either.right(newUser);
  }

  /**
   * (Soft) Delete a User.
   *
   * @param user The user to delete.
   */
  public void delete(User user) {
    user.setStatus(Status.deleted);
    userRepository.update(user);
  }

  public User addTracklist(User user) {
    user.addTracklist();
    user.update();

    return user;
  }

  /**
   * Log a user into the system.
   * Takes user credentials and returns an access token.
   *
   * @param loginUser The user's credentials.
   * @return Either an access token on success or validation errors.
   */
  public Either<Map<String, List<ValidationError>>, Token> loginToken(LoginUser loginUser) {
    return userRepository.findByEmail(loginUser.getEmail())
        .flatMap(user -> isAuthorised(user, loginUser.getPassword()))
        .map(Either::<Map<String, List<ValidationError>>, Token>right)
        .orElse(Either.left(getValidationErrors(FIELD_LOGIN, MESSAGE_LOGIN_FAILURE)));
  }

  private Optional<Token> isAuthorised(User user, String plaintext) {
    return user.isAuthorised(plaintext)
        ? Optional.of(tokenService.generate(user))
        : Optional.empty();
  }

  /**
   * Helper method to aid in generating validation error responses.
   *
   * @param key           The field name that failed.
   * @param errorMessage  The validation error.
   * @return A collection of validation errors.
   */
  private Map<String, List<ValidationError>> getValidationErrors(String key, String errorMessage) {
    return ImmutableMap.of(key, ImmutableList.of(new ValidationError(key, errorMessage)));
  }
}
