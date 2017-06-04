package repositories;

import com.avaje.ebean.Model.Finder;
import java.util.List;
import java.util.Optional;
import models.Token;

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

  @Override
  public void insert(Token token) {
    token.insert();
  }

  @Override
  public void update(Token token) {
    token.update();
  }

  @Override
  public void delete(Token token) {
    token.delete();
  }
}
