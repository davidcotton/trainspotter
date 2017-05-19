package services;

import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import models.User;
import play.data.FormFactory;
import repositories.UserRepository;

public class UserService {

  private final UserRepository userRepository;
  private final FormFactory formFactory;

  @Inject
  public UserService(UserRepository userRepository, FormFactory formFactory) {
    this.userRepository = userRepository;
    this.formFactory = formFactory;
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


}
