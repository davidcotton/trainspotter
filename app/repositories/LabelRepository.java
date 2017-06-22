package repositories;

import com.avaje.ebean.Model.Finder;
import com.avaje.ebean.PagedList;

import java.util.List;
import java.util.Optional;
import models.Label;

public class LabelRepository implements Repository<Label> {

  /** Ebean helper */
  private static Finder<Long, Label> find = new Finder<>(Label.class);

  private static final int PAGE_SIZE = 12;

  @Override
  public List<Label> findAll() {
    return find.orderBy().asc("name").findList();
  }

  /**
   * Fetch a paginated collection of Labels.
   *
   * @param page The page number to fetch (offset).
   * @return A paginated list of Labels ordered by name.
   */
  public PagedList<Label> findAllPaged(int page) {
    return find
        .orderBy("name")
        .findPagedList(--page, PAGE_SIZE); // page -1 so we can be 1-indexed
  }

  @Override
  public Optional<Label> findById(long id) {
    return Optional.ofNullable(find.byId(id));
  }

  /**
   * Find a Label by its name.
   *
   * @param name  The name of the Label to search for.
   * @return      An optional Label if found.
   */
  public Optional<Label> findByName(String name) {
    return Optional.ofNullable(find.where().eq("name", name).findUnique());
  }

  /**
   * Find an Label by their slug.
   *
   * @param slug The slug of the Label.
   * @return An optional Label if found.
   */
  public Optional<Label> findBySlug(String slug) {
    return Optional.ofNullable(find.where().eq("slug", slug).findUnique());
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
    label.delete();
  }
}
