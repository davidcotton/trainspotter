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
    return find.orderBy().asc("name").findList();
  }

  @Override
  public Optional<Genre> findById(long id) {
    return Optional.ofNullable(find.byId(id));
  }

  /**
   * Find a Genre by its name.
   *
   * @param name  The name of the Genre to search for.
   * @return      An optional Genre if found.
   */
  public Optional<Genre> findByName(String name) {
    return Optional.ofNullable(find.where().eq("name", name).findUnique());
  }

  /**
   * Find an Genre by their slug.
   *
   * @param slug The slug of the Genre.
   * @return An optional Genre if found.
   */
  public Optional<Genre> findBySlug(String slug) {
    return Optional.ofNullable(find.where().eq("slug", slug).findUnique());
  }

  @Override
  public void insert(Genre genre) {
    genre.insert();
  }

  @Override
  public void update(Genre genre) {
    genre.update();
  }

  @Override
  public void delete(Genre genre) {
    genre.delete();
  }
}
