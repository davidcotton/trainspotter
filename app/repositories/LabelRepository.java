package repositories;

import com.avaje.ebean.Model.Finder;
import java.util.List;
import java.util.Optional;
import models.Label;

public class LabelRepository implements Repository<Label> {

  /** Ebean helper */
  private static Finder<Long, Label> find = new Finder<>(Label.class);

  @Override
  public List<Label> findAll() {
    return find.all();
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
