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
    return find.all();
  }

  @Override
  public Optional<Track> findById(long id) {
    return Optional.ofNullable(find.byId(id));
  }

  @Override
  public void insert(Track track) {
    track.insert();
  }

  @Override
  public void update(Track track) {
    track.update();
  }
}
