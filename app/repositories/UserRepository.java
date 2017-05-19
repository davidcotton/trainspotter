package repositories;

import com.avaje.ebean.Model.Finder;
import java.util.List;
import java.util.Optional;
import models.User;

public class UserRepository implements Repository<User> {

  /** Ebean helper */
  private static Finder<Long, User> find = new Finder<>(User.class);

  @Override
  public List<User> findAll() {
    return find.all();
  }

  @Override
  public Optional<User> findById(long id) {
    return Optional.ofNullable(find.byId(id));
  }

  public Optional<User> findByEmail(String email) {
    return Optional.ofNullable(find.where().eq("email", email).findUnique());
  }

  @Override
  public void insert(User user) {
    user.insert();
  }

  @Override
  public void update(User user) {
    user.update();
  }
}
