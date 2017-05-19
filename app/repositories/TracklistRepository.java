package repositories;

import com.avaje.ebean.Model.Finder;

import java.util.List;
import java.util.Optional;
import models.Tracklist;

public class TracklistRepository implements Repository<Tracklist> {

  /** Ebean helper */
  private static Finder<Long, Tracklist> find = new Finder<>(Tracklist.class);

  @Override
  public List<Tracklist> findAll() {
    return find.all();
  }

  @Override
  public Optional<Tracklist> findById(long id) {
    return Optional.ofNullable(find.byId(id));
  }

  @Override
  public void insert(Tracklist tracklist) {
    tracklist.insert();
  }

  @Override
  public void update(Tracklist tracklist) {
    tracklist.update();
  }
}
