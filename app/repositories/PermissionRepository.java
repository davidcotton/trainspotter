package repositories;

import com.avaje.ebean.Model.Finder;
import java.util.List;
import java.util.Optional;
import models.Permission;

public class PermissionRepository implements Repository<Permission> {

  /** Ebean helper */
  private static Finder<Long, Permission> find = new Finder<>(Permission.class);

  @Override
  public List<Permission> findAll() {
    return find.all();
  }

  @Override
  public Optional<Permission> findById(long id) {
    return Optional.ofNullable(find.byId(id));
  }

  @Override
  public void insert(Permission permission) {
    permission.insert();
  }

  @Override
  public void update(Permission permission) {
    permission.update();
  }
}
