package repositories;

import com.avaje.ebean.Model.Finder;
import com.avaje.ebean.PagedList;

import java.util.List;
import java.util.Optional;
import models.Tracklist;

public class TracklistRepository implements Repository<Tracklist> {

  /** Ebean helper */
  private static Finder<Long, Tracklist> find = new Finder<>(Tracklist.class);

  private static final int PAGE_SIZE = 5;

  @Override
  public List<Tracklist> findAll() {
    return find.orderBy().desc("performed").findList();
  }

  public PagedList<Tracklist> findAllPaged(int page) {
    return find
        .orderBy().desc("performed")
        .findPagedList(page, PAGE_SIZE);
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

  @Override
  public void delete(Tracklist tracklist) {
    tracklist.delete();
  }
}
