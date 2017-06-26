package repositories;

import com.avaje.ebean.Model.Finder;
import com.avaje.ebean.PagedList;
import java.util.List;
import java.util.Optional;
import models.Artist;
import models.Track;
import models.Track.Status;

public class TrackRepository implements Repository<Track> {

  /** Ebean helper */
  private static Finder<Long, Track> find = new Finder<>(Track.class);
  private static final int PAGE_SIZE = 10;

  @Override
  public List<Track> findAll() {
    return find
        .where().ne("status", Status.deleted)
        .orderBy("name")
        .findList();
  }

  public PagedList<Track> findAllPagedByArtist(Artist artist, int page) {
    return find
        .fetch("artists")
        .where().eq("artists.id", artist.getId())
        .where().ne("status", Status.deleted)
        .orderBy().desc("performed")
        .findPagedList(--page, PAGE_SIZE);
  }

  @Override
  public Optional<Track> findById(long id) {
    return Optional.ofNullable(
        find
            .where().idEq(id)
            .where().ne("status", Status.deleted)
            .findUnique()
    );
  }

  /**
   * Find a Track by its name.
   *
   * @param name The name of the Track to search for.
   * @return An optional Track if found.
   */
  public Optional<Track> findByName(String name) {
    return Optional.ofNullable(
        find
            .where().eq("name", name)
            .where().ne("status", Status.deleted)
            .findUnique()
    );
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
    track.setStatus(Status.deleted);
    track.update();
  }
}
