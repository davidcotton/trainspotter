package services;

import com.fasterxml.jackson.databind.JsonNode;

import static java.util.Objects.requireNonNull;
import static play.libs.Json.toJson;

import io.atlassian.fugue.Either;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.inject.Inject;
import models.User;
import models.User.InsertValidators;
import models.User.UpdateValidators;
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
   * Fetch all Users.
   *
   * @return A collection of all Users in the database.
   */
  public List<User> fetchAll() {
    return userRepository.findAll();
  }

  /**
   * Find a User by their ID.
   *
   * @param id  The ID to search for.
   * @return    An optional User if found.
   */
  public Optional<User> findById(long id) {
    return userRepository.findById(id);
  }

  /**
   * Find a User by their email address.
   *
   * @param email The email address to search for.
   * @return      An optional User if found.
   */
  public Optional<User> findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  /**
   * Create a new User in the system.
   *
   * @param user  The user supplied data.
   * @return      Either the inserted User or validation errors.
   */
  public Either<Map<String, List<ValidationError>>, User> insert(User user) {
    // set default values
    user.setStatus(User.Status.unverified);

    // validate new user
    JsonNode jsonNode = toJson(user);
    Form<User> userForm = formFactory.form(User.class, InsertValidators.class).bind(jsonNode);
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
   * Update a User.
   *
   * @param savedUser The existing User data.
   * @param newUser   The new User data.
   * @return          Either the updated User or validation errors.
   */
  public Either<Map<String, List<ValidationError>>, User> update(User savedUser, User newUser) {
    // copy over read only fields
    newUser.setId(savedUser.getId());
    newUser.setPassword(savedUser.getPassword());
    newUser.setStatus(savedUser.getStatus());
    newUser.setCreated(savedUser.getCreated());

    // validate the changes
    Form<User> userForm = formFactory.form(User.class, UpdateValidators.class).bind(toJson(newUser));
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
