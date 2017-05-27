package repositories;

import com.avaje.ebean.Model.Finder;
import java.util.List;
import java.util.Optional;
import models.Artist;

public class ArtistRepository implements Repository<Artist> {

  /** Ebean helper */
  private static Finder<Long, Artist> find = new Finder<>(Artist.class);

  @Override
  public List<Artist> findAll() {
    return find.all();
  }

  @Override
  public Optional<Artist> findById(long id) {
    return Optional.ofNullable(find.byId(id));
  }

  /**
   * Find an Artist by their name.
   *
   * @param name  The name of the Artist to search for.
   * @return      An optional Artist if found.
   */
  public Optional<Artist> findByName(String name) {
    return Optional.ofNullable(find.where().eq("name", name).findUnique());
  }

  @Override
  public void insert(Artist artist) {
    artist.insert();
  }

  @Override
  public void update(Artist artist) {
    artist.update();
  }
}
