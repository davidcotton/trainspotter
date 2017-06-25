package repositories;

import com.avaje.ebean.Model.Finder;
import com.avaje.ebean.PagedList;
import java.util.List;
import java.util.Optional;
import models.Artist;
import models.Artist.Status;

public class ArtistRepository implements Repository<Artist> {

  /** Ebean helper */
  private static Finder<Long, Artist> find = new Finder<>(Artist.class);
  private static final int PAGE_SIZE = 16;

  @Override
  public List<Artist> findAll() {
    return find
        .where().ne("status", Status.deleted)
        .orderBy("name")
        .findList();
  }

  /**
   * Fetch a paginated collection of Artists.
   *
   * @param page The page number to fetch (offset).
   * @return A paginated list of Artists ordered by name.
   */
  public PagedList<Artist> findAllPaged(int page) {
    return find
        .where().ne("status", Status.deleted)
        .orderBy("name")
        .findPagedList(--page, PAGE_SIZE); // page -1 so we can be 1-indexed
  }

  @Override
  public Optional<Artist> findById(long id) {
    return Optional.ofNullable(find.byId(id));
  }

  /**
   * Find an Artist by their name.
   *
   * @param name  The name of the Artist to search for.
   * @return An optional Artist if found.
   */
  public Optional<Artist> findByName(String name) {
    return Optional.ofNullable(find.where().eq("name", name).findUnique());
  }

  /**
   * Find an Artist by their slug.
   *
   * @param slug The slug of the Artist.
   * @return An optional Artist if found.
   */
  public Optional<Artist> findBySlug(String slug) {
    return Optional.ofNullable(
        find
            .where().eq("slug", slug)
            .where().ne("status", Status.deleted)
            .findUnique()
    );
  }

  @Override
  public void insert(Artist artist) {
    artist.insert();
  }

  @Override
  public void update(Artist artist) {
    artist.update();
  }

  @Override
  public void delete(Artist artist) {
    artist.setStatus(Status.deleted);
    artist.update();
  }
}
