package repositories;

import com.avaje.ebean.Model.Finder;
import java.util.List;
import java.util.Optional;
import models.Token;
import models.User;

public class TokenRepository implements Repository<Token> {

  /** Ebean helper */
  private static Finder<Long, Token> find = new Finder<>(Token.class);

  @Override
  public List<Token> findAll() {
    return find.all();
  }

  @Override
  public Optional<Token> findById(long id) {
    return Optional.ofNullable(find.byId(id));
  }

  /**
   * Find a token by User.
   *
   * @param user The user to search for.
   * @return An optional Token if found.
   */
  public Optional<Token> findByUser(User user) {
    return Optional.ofNullable(find.where().eq("user_id", user.getId()).findUnique());
  }

  /**
   * Fetch a token by its user ID.
   *
   * @param userId The user ID to search for.
   * @return An optional Token if found.
   */
  public Optional<Token> findByUserId(long userId) {
    return Optional.ofNullable(find.where().eq("user_id", userId).findUnique());
  }

  @Override
  public void insert(Token token) {
    token.insert();
  }

  @Override
  public void update(Token token) {
    token.update();
  }
}
