package repositories;

import com.avaje.ebean.Model.Finder;
import java.util.List;
import java.util.Optional;
import models.User;
import models.User.Status;

public class UserRepository implements Repository<User> {

  /** Ebean helper */
  private static Finder<Long, User> find = new Finder<>(User.class);

  @Override
  public List<User> findAll() {
    return find.all();
  }

  /**
   * Find all non-deleted users.
   *
   * @return A collection of non-deleted users.
   */
  public List<User> findAllCurrentUsers() {
    return find.where().ne("status", Status.deleted).findList();
  }

  @Override
  public Optional<User> findById(long id) {
    return Optional.ofNullable(
        find
            .where().idEq(id)
            // filter out users that have been 'deleted'
            .where().ne("status", Status.deleted)
            .findUnique()
    );
  }

  /**
   * Find a User by their email address.
   *
   * @param email The email address to search for.
   * @return      An optional user if found.
   */
  public Optional<User> findByEmail(String email) {
    return Optional.ofNullable(
        find
            .where().eq("email", email)
            // filter out users that have been 'deleted'
            .where().ne("status", Status.deleted)
            .findUnique()
    );
  }

  @Override
  public void insert(User user) {
    user.insert();
  }

  @Override
  public void update(User user) {
    user.update();
  }

  @Override
  public void delete(User user) {
    user.delete();
  }
}
