package repositories;

import com.avaje.ebean.Model.Finder;
import com.avaje.ebean.PagedList;
import java.util.List;
import java.util.Optional;
import models.Label;
import models.Label.Status;

public class LabelRepository implements Repository<Label> {

  /** Ebean helper */
  private static Finder<Long, Label> find = new Finder<>(Label.class);
  private static final int PAGE_SIZE = 12;

  @Override
  public List<Label> findAll() {
    return find
        .where().ne("status", Status.deleted)
        .orderBy("name")
        .findList();
  }

  /**
   * Fetch a paginated collection of Labels.
   *
   * @param page The paginator page number.
   * @return A paginated list of Labels ordered by name.
   */
  public PagedList<Label> findAllPaged(int page) {
    return find
        .where().ne("status", Status.deleted)
        .orderBy("name")
        .findPagedList(--page, PAGE_SIZE); // page -1 so we can be 1-indexed
  }

  @Override
  public Optional<Label> findById(long id) {
    return Optional.ofNullable(find.byId(id));
  }

  /**
   * Search for a Label by its name.
   *
   * @param name The name of the Label to search for.
   * @return An optional Label if found.
   */
  public Optional<Label> findByName(String name) {
    return Optional.ofNullable(
        find
            .where().ne("status", Status.deleted)
            .where().eq("name", name)
            .findUnique()
    );
  }

  /**
   * Search for a Label by its slug.
   *
   * @param slug The slug of the Label.
   * @return An optional Label if found.
   */
  public Optional<Label> findBySlug(String slug) {
    return Optional.ofNullable(
        find
            .where().eq("slug", slug)
            .where().ne("status", Status.deleted)
            .findUnique()
    );
  }

  @Override
  public void insert(Label label) {
    label.insert();
  }

  @Override
  public void update(Label label) {
    label.update();
  }

  @Override
  public void delete(Label label) {
    label.setStatus(Status.deleted);
    label.update();
  }
}
