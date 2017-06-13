package controllers;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import javax.inject.Inject;
import models.Channel;
import play.mvc.Controller;
import play.mvc.Result;
import repositories.ChannelRepository;
import views.html.channel.index;
import views.html.channel.view;

public class ChannelController extends Controller {

  private final ChannelRepository channelRepository;

  @Inject
  public ChannelController(ChannelRepository channelRepository) {
    this.channelRepository = requireNonNull(channelRepository);
  }

  /**
   * View all channels.
   *
   * @return A page with all channels.
   */
  public Result index() {
    return ok(index.render(channelRepository.findAll()));
  }

  /**
   * View a single channel.
   *
   * @param id The channel's ID.
   * @return An channel page if found.
   */
  public Result view(long id) {
    Optional<Channel> maybeChannel = channelRepository.findById(id);
    return ok(view.render(maybeChannel.get()));
  }

  public Result add() {
    return TODO;
  }

  public Result edit(long id) {
    return TODO;
  }

  public Result delete(long id) {
    return TODO;
  }
}
