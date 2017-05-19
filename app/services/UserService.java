package services;

import static java.util.Objects.requireNonNull;
import static play.libs.Json.toJson;

import io.atlassian.fugue.Either;
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

  public List<User> findAll() {
    return userRepository.findAll();
  }

  public Optional<User> findById(long id) {
    return userRepository.findById(id);
  }

  public Optional<User> findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  public Either<Map<String, List<ValidationError>>, User> insert(User user) {
    Form<User> userForm = formFactory.form(User.class).bind(toJson(user));
    if (userForm.hasErrors()) {
      return Either.left(userForm.errors());
    }

    userRepository.insert(user);

    return Either.right(user);
  }

  public Either<Map<String, List<ValidationError>>, User> update(User user) {
    Form<User> userForm = formFactory.form(User.class).bind(toJson(user));
    if (userForm.hasErrors()) {
      return Either.left(userForm.errors());
    }

    userRepository.update(user);

    return Either.right(user);
  }

}
