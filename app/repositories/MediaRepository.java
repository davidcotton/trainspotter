package repositories;

import com.avaje.ebean.Model.Finder;
import java.util.List;
import java.util.Optional;
import models.Media;

public class MediaRepository implements Repository<Media> {

  /** Ebean helper */
  private static Finder<Long, Media> find = new Finder<>(Media.class);

  @Override
  public List<Media> findAll() {
    return find.orderBy().asc("created").findList();
  }

  @Override
  public Optional<Media> findById(long id) {
    return Optional.ofNullable(find.byId(id));
  }

  @Override
  public void insert(Media media) {
    media.insert();
  }

  @Override
  public void update(Media media) {
    media.update();
  }
}
