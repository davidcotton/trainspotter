package repositories;

import com.avaje.ebean.Model.Finder;
import com.avaje.ebean.PagedList;

import java.util.List;
import java.util.Optional;
import models.Program;

public class ProgramRepository implements Repository<Program> {

  /** Ebean helper */
  private static Finder<Long, Program> find = new Finder<>(Program.class);

  private static final int PAGE_SIZE = 12;

  @Override
  public List<Program> findAll() {
    return find.orderBy().asc("name").findList();
  }

  /**
   * Find all Programs that belong to a Channel.
   *
   * @param channelId The Channel ID.
   * @return A Collection of Programs.
   */
  public List<Program> findByChannel(long channelId) {
    return find
        .where().eq("channel_id", channelId)
        .orderBy().asc("name")
        .findList();
  }

  /**
   * Fetch a paginated collection of Programs.
   *
   * @param page The page number to fetch (offset).
   * @return A paginated list of Programs ordered by name.
   */
  public PagedList<Program> findAllPaged(int page) {
    return find
        .orderBy("name")
        .findPagedList(--page, PAGE_SIZE); // page -1 so we can be 1-indexed
  }

  @Override
  public Optional<Program> findById(long id) {
    return Optional.ofNullable(find.byId(id));
  }

  /**
   * Find a Program by its name.
   *
   * @param name  The name of the Program to search for.
   * @return      An optional Program if found.
   */
  public Optional<Program> findByName(String name) {
    return Optional.ofNullable(find.where().eq("name", name).findUnique());
  }

  /**
   * Find an Program by their slug.
   *
   * @param slug The slug of the Program.
   * @return An optional Program if found.
   */
  public Optional<Program> findBySlug(String slug) {
    return Optional.ofNullable(find.where().eq("slug", slug).findUnique());
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
