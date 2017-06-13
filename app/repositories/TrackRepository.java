package repositories;

import com.avaje.ebean.Model.Finder;
import java.util.List;
import java.util.Optional;
import models.Track;

public class TrackRepository implements Repository<Track> {

  /** Ebean helper */
  private static Finder<Long, Track> find = new Finder<>(Track.class);

  @Override
  public List<Track> findAll() {
    return find.orderBy().asc("name").findList();
  }

  @Override
  public Optional<Track> findById(long id) {
    return Optional.ofNullable(find.byId(id));
  }

  /**
   * Find a Track by its name.
   *
   * @param name The name of the Track to search for.
   * @return An optional Track if found.
   */
  public Optional<Track> findByName(String name) {
    return Optional.ofNullable(find.where().eq("name", name).findUnique());
  }

  @Override
  public void insert(Track track) {
    track.insert();
  }

  @Override
  public void update(Track track) {
    track.update();
  }

  @Override
  public void delete(Track track) {
    track.delete();
  }
}
