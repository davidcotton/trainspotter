package repositories;

import com.avaje.ebean.Model.Finder;
import com.avaje.ebean.PagedList;
import java.util.List;
import java.util.Optional;
import models.Artist;
import models.Genre;
import models.Tracklist;

public class TracklistRepository implements Repository<Tracklist> {

  /** Ebean helper */
  private static Finder<Long, Tracklist> find = new Finder<>(Tracklist.class);

  private static final int PAGE_SIZE = 10;

  @Override
  public List<Tracklist> findAll() {
    return find.orderBy().desc("performed").findList();
  }

  public PagedList<Tracklist> findAllPaged(int page) {
    return find
        .orderBy().desc("performed")
        .findPagedList(--page, PAGE_SIZE);
  }

  public PagedList<Tracklist> findAllPagedByArtist(Artist artist, int page) {
    return find
        .fetch("artists")
        .where().eq("artists.id", artist.getId())
        .orderBy().desc("performed")
        .findPagedList(--page, PAGE_SIZE);
  }

  public PagedList<Tracklist> findAllPagedByGenre(Genre genre, int page) {
    return find
        .fetch("genres")
        .where().eq("genres.id", genre.getId())
        .orderBy().desc("performed")
        .findPagedList(--page, PAGE_SIZE);
  }

  @Override
  public Optional<Tracklist> findById(long id) {
    return Optional.ofNullable(find.byId(id));
  }

  /**
   * Find an Tracklist by their slug.
   *
   * @param slug The slug of the Tracklist.
   * @return An optional Tracklist if found.
   */
  public Optional<Tracklist> findBySlug(String slug) {
    return Optional.ofNullable(find.where().eq("slug", slug).findUnique());
  }

  @Override
  public void insert(Tracklist tracklist) {
    tracklist.insert();
  }

  @Override
  public void update(Tracklist tracklist) {
    tracklist.update();
  }

  @Override
  public void delete(Tracklist tracklist) {
    tracklist.delete();
  }
}
