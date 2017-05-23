package repositories;

import com.avaje.ebean.Model.Finder;
import java.util.List;
import java.util.Optional;
import models.Genre;

public class GenreRepository implements Repository<Genre> {

  /** Ebean helper */
  private static Finder<Long, Genre> find = new Finder<>(Genre.class);

  @Override
  public List<Genre> findAll() {
    return find.all();
  }

  @Override
  public Optional<Genre> findById(long id) {
    return Optional.ofNullable(find.byId(id));
  }

  @Override
  public void insert(Genre genre) {
    genre.insert();
  }

  @Override
  public void update(Genre genre) {
    genre.update();
  }
}
