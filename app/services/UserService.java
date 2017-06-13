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
import models.CreateUser;
import models.LoginUser;
import models.Token;
import models.User;
import models.User.Status;
import play.data.Form;
import play.data.FormFactory;
import play.data.validation.ValidationError;
import repositories.UserRepository;

public class UserService {

  private final UserRepository userRepository;
  private final FormFactory formFactory;
  private final AuthenticationService authenticationService;
  private final TokenService tokenService;
  private static final String FIELD_LOGIN = "login";
  private static final String MESSAGE_LOGIN_FAILURE = "Unable to login";

  @Inject
  public UserService(
      UserRepository userRepository,
      FormFactory formFactory,
      AuthenticationService authenticationService,
      TokenService tokenService
  ) {
    this.userRepository = requireNonNull(userRepository);
    this.formFactory = requireNonNull(formFactory);
    this.authenticationService = requireNonNull(authenticationService);
    this.tokenService = requireNonNull(tokenService);
  }

  /**
   * Fetch all Users.
   *
   * @return A collection of all Users in the database.
   */
  public List<User> fetchAll() {
    return userRepository.findAllActiveUsers();
  }

  /**
   * Find a User by their ID.
   *
   * @param id  The ID to search for.
   * @return An optional User if found.
   */
  public Optional<User> findById(long id) {
    return userRepository.findActiveById(id);
  }

  /**
   * Find a User by their email address.
   *
   * @param email The email address to search for.
   * @return An optional User if found.
   */
  Optional<User> findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  /**
   * Create a new User in the system.
   *
   * @param createUser  The user supplied data.
   * @return Either the inserted User or validation errors.
   */
  public Either<Map<String, List<ValidationError>>, User> insert(CreateUser createUser) {
    // validate new user
    Form<CreateUser> userForm = formFactory
        .form(CreateUser.class, CreateUser.InsertValidators.class)
        .bind(toJson(createUser));
    if (userForm.hasErrors()) {
      // return validation errors
      return Either.left(userForm.errors());
    }

    // create a user object from our createUser object
    User user = new User(createUser);

    // save to DB
    userRepository.insert(user);

    // return the saved user
    return Either.right(user);
  }

  /**
   * Update a User.
   *
   * @param savedUser   The existing User data.
   * @param createUser  The new User data.
   * @return Either the updated User or validation errors.
   */
  public Either<Map<String, List<ValidationError>>, User> update(User savedUser, CreateUser createUser) {
    // validate the changes
    Form<CreateUser> userForm = formFactory
        .form(CreateUser.class, CreateUser.UpdateValidators.class)
        .bind(toJson(createUser));
    if (userForm.hasErrors()) {
      // return validation errors
      return Either.left(userForm.errors());
    }

    User newUser = new User(createUser);
    // copy over read only fields
    newUser.setId(savedUser.getId());
    newUser.setStatus(savedUser.getStatus());
    newUser.setCreated(savedUser.getCreated());

    // save to DB
    userRepository.update(newUser);

    // return saved user
    return Either.right(newUser);
  }

  /**
   * Delete a User.
   * Use a soft deletes approach.
   *
   * @param user  The user to delete.
   */
  public void delete(User user) {
    user.setStatus(Status.deleted);
    userRepository.update(user);
  }

  /**
   * Log a user into the system.
   * Takes user credentials and returns an access token.
   *
   * @param loginUser The user's credentials.
   * @return Either an access token on success or validation errors.
   */
  public Either<Map<String, List<ValidationError>>, Token> login(LoginUser loginUser) {
    // validate the login details
    Form<LoginUser> userForm = formFactory
        .form(LoginUser.class, LoginUser.Validators.class)
        .bind(toJson(loginUser));
    if (userForm.hasErrors()) {
      return Either.left(userForm.errors());
    }

    return userRepository.findByEmail(loginUser.getEmail())
        .flatMap(user -> fetchToken(loginUser.getPassword(), user))
        .map(Either::<Map<String, List<ValidationError>>, Token>right)
        .orElse(Either.left(getValidationErrors(FIELD_LOGIN, MESSAGE_LOGIN_FAILURE)));
  }

  /**
   * Fetch a login token for a user if the supplied password matches their saved hash.
   *
   * @param user      The user to verify.
   * @param password  The password to check.
   * @return An optional token that will be empty if the password doesn't match.
   */
  private Optional<Token> fetchToken(String password, User user) {
    return authenticationService.checkPassword(password, user.getHash())
        ? Optional.of(tokenService.create(user))
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
