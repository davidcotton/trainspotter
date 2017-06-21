package repositories;

import com.avaje.ebean.Model.Finder;
import java.util.List;
import java.util.Optional;
import models.Channel;

public class ChannelRepository implements Repository<Channel> {

  /** Ebean helper */
  private static Finder<Long, Channel> find = new Finder<>(Channel.class);

  @Override
  public List<Channel> findAll() {
    return find.orderBy().asc("name").findList();
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
   * Find an Channel by its route.
   *
   * @param route The route of the Channel.
   * @return An optional Channel if found.
   */
  public Optional<Channel> findByRoute(String route) {
    return Optional.ofNullable(find.where().eq("route", route).findUnique());
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
