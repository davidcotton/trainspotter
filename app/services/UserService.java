package services;

import static java.util.Objects.requireNonNull;
import static play.libs.Json.toJson;

import io.atlassian.fugue.Either;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.inject.Inject;
import models.CreateUser;
import models.User;
import models.User.Status;
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
    return userRepository.findAllCurrentUsers();
  }

  /**
   * Find a User by their ID.
   *
   * @param id  The ID to search for.
   * @return An optional User if found.
   */
  public Optional<User> findById(long id) {
    return userRepository.findById(id);
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
}
