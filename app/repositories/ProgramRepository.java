package repositories;

import com.avaje.ebean.Model.Finder;
import java.util.List;
import java.util.Optional;
import models.Program;

public class ProgramRepository implements Repository<Program> {

  /** Ebean helper */
  private static Finder<Long, Program> find = new Finder<>(Program.class);

  @Override
  public List<Program> findAll() {
    return find.orderBy().asc("name").findList();
  }

  @Override
  public Optional<Program> findById(long id) {
    return Optional.ofNullable(find.byId(id));
  }

  @Override
  public void insert(Program program) {
    program.insert();
  }

  @Override
  public void update(Program program) {
    program.update();
  }

  @Override
  public void delete(Program program) {
    program.delete();
  }
}
