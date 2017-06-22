package repositories;

import com.avaje.ebean.Model.Finder;
import com.avaje.ebean.PagedList;

import java.util.List;
import java.util.Optional;
import models.Channel;

public class ChannelRepository implements Repository<Channel> {

  /** Ebean helper */
  private static Finder<Long, Channel> find = new Finder<>(Channel.class);

  private static final int PAGE_SIZE = 12;

  @Override
  public List<Channel> findAll() {
    return find.orderBy().asc("name").findList();
  }

  /**
   * Fetch a paginated collection of Channels.
   *
   * @param page The page number to fetch (offset).
   * @return A paginated list of Channels ordered by name.
   */
  public PagedList<Channel> findAllPaged(int page) {
    return find
        .orderBy("name")
        .findPagedList(--page, PAGE_SIZE); // page -1 so we can be 1-indexed
  }

  @Override
  public Optional<Channel> findById(long id) {
    return Optional.ofNullable(find.byId(id));
  }

  /**
   * Find a Channel by its name.
   *
   * @param name  The name of the Channel to search for.
   * @return      An optional Channel if found.
   */
  public Optional<Channel> findByName(String name) {
    return Optional.ofNullable(find.where().eq("name", name).findUnique());
  }

  /**
   * Find an Channel by its slug.
   *
   * @param slug The slug of the Channel.
   * @return An optional Channel if found.
   */
  public Optional<Channel> findBySlug(String slug) {
    return Optional.ofNullable(find.where().eq("slug", slug).findUnique());
  }

  @Override
  public void insert(Channel channel) {
    channel.insert();
  }

  @Override
  public void update(Channel channel) {
    channel.update();
  }

  @Override
  public void delete(Channel channel) {
    channel.delete();
  }
}
