package repositories;

import com.avaje.ebean.Model.Finder;
import com.avaje.ebean.PagedList;
import java.util.List;
import java.util.Optional;
import models.Artist;
import models.Genre;
import models.Tracklist;
import models.Tracklist.Status;

public class TracklistRepository implements Repository<Tracklist> {

  /** Ebean helper */
  private static Finder<Long, Tracklist> find = new Finder<>(Tracklist.class);
  private static final int PAGE_SIZE = 10;

  @Override
  public List<Tracklist> findAll() {
    return find
        .where().ne("status", Status.deleted)
        .orderBy().desc("performed")
        .findList();
  }

  /**
   * Fetch a paginated collection of ALL Tracklists.
   *
   * @param page The paginator page number.
   * @return A paginated collection of Tracklists.
   */
  public PagedList<Tracklist> findAllPaged(int page) {
    return find
        .where().ne("status", Status.deleted)
        .orderBy().desc("performed")
        .findPagedList(--page, PAGE_SIZE);
  }

  /**
   * Fetch a paged collection of Tracklists performed by an Artist.
   *
   * @param artist  The Artist to have performed the Tracklists.
   * @param page    The paginator page number.
   * @return A paginated collection of Tracklists.
   */
  public PagedList<Tracklist> findAllPagedByArtist(Artist artist, int page) {
    return find
        .fetch("artists")
        .where().eq("artists.id", artist.getId())
        .where().ne("status", Status.deleted)
        .orderBy().desc("performed")
        .findPagedList(--page, PAGE_SIZE);
  }

  /**
   * Fetch the most popular tracklists.
   *
   * @todo fix this to find the most popular.
   *
   * @return A collection of the most popular tracklists.
   */
  public List<Tracklist> findMostPopular() {
    // hard coded to performed date until I add "popularity" features
    return find
        .orderBy().desc("performed")
        .setMaxRows(10)
        .findList();
  }

  public PagedList<Tracklist> findAllPagedByGenre(Genre genre, int page) {
    return find
        .fetch("genres")
        .where().eq("genres.id", genre.getId())
        .where().ne("status", Status.deleted)
        .orderBy().desc("performed")
        .findPagedList(--page, PAGE_SIZE);
  }

  @Override
  public Optional<Tracklist> findById(long id) {
    return Optional.ofNullable(
        find
            .where().idEq(id)
            .where().ne("status", Status.deleted)
            .findUnique()
    );
  }

  /**
   * Find an Tracklist by their slug.
   *
   * @param slug The slug of the Tracklist.
   * @return An optional Tracklist if found.
   */
  public Optional<Tracklist> findBySlug(String slug) {
    return Optional.ofNullable(
        find
            .where().eq("slug", slug)
            .where().ne("status", Status.deleted)
            .findUnique()
    );
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
