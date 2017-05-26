package services;

import static java.util.Objects.requireNonNull;
import static play.libs.Json.toJson;

import io.atlassian.fugue.Either;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.inject.Inject;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.data.validation.ValidationError;
import repositories.UserRepository;

public class UserService {

  private final UserRepository userRepository;
  private final FormFactory formFactory;

  @Inject
  public UserService(UserRepository userRepository, FormFactory formFactory) {
    this.userRepository = requireNonNull(userRepository);
    this.formFactory = requireNonNull(formFactory);
  }

  /**
   * Fetch all users.
   * @return A collection of all users in the database.
   */
  public List<User> fetchAll() {
    return userRepository.findAll();
  }

  /**
   * Find a user by their ID.
   * @param id  The ID to search for.
   * @return    An optional user if found.
   */
  public Optional<User> findById(long id) {
    return userRepository.findById(id);
  }

  /**
   * Find a user by their email address.
   * @param email The email address to search for.
   * @return      An optional user if found.
   */
  public Optional<User> findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  /**
   * Insert a new user.
   * @param user  The user to insert.
   * @return      Either the inserted user or validation errors.
   */
  public Either<Map<String, List<ValidationError>>, User> insert(User user) {
    // validate new user
    Form<User> userForm = formFactory
        .form(User.class, User.InsertValidators.class)
        .bind(toJson(user));
    if (userForm.hasErrors()) {
      // return validation errors
      return Either.left(userForm.errors());
    }

    // save to DB
    userRepository.insert(user);

    // return the saved user
    return Either.right(user);
  }

  /**
   * Update a user.
   * @param savedUser The existing user data.
   * @param newUser   The new user data.
   * @return          Either the updated user or validation errors.
   */
  public Either<Map<String, List<ValidationError>>, User> update(User savedUser, User newUser) {
    // copy over read only fields
    newUser.setId(savedUser.getId());
    newUser.setSalt(savedUser.getSalt());
    newUser.setStatus(savedUser.getStatus());
    newUser.setCreated(savedUser.getCreated());

    // validate the changes
    Form<User> userForm = formFactory
        .form(User.class, User.UpdateValidators.class)
        .bind(toJson(newUser));
    if (userForm.hasErrors()) {
      // return validation errors
      return Either.left(userForm.errors());
    }

    // save to DB
    userRepository.update(newUser);

    // return saved user
    return Either.right(newUser);
  }

}
