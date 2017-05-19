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

  @Override
  public void insert(Artist artist) {
    artist.insert();
  }

  @Override
  public void update(Artist artist) {
    artist.update();
  }
}
