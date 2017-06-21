package controllers;

import static java.util.Objects.requireNonNull;

import javax.inject.Inject;
import models.Channel;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import repositories.ChannelRepository;
import views.html.channel.add;
import views.html.channel.edit;
import views.html.channel.index;
import views.html.channel.view;
import views.html.notFound;

public class ChannelController extends Controller {

  private final ChannelRepository channelRepository;
  private final FormFactory formFactory;

  @Inject
  public ChannelController(ChannelRepository channelRepository, FormFactory formFactory) {
    this.channelRepository = requireNonNull(channelRepository);
    this.formFactory = requireNonNull(formFactory);
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
   * @param slug The slug of the channel.
   * @return A channel page if found.
   */
  public Result view(String slug) {
    return channelRepository
        .findByRoute(slug)
        .map(channel -> ok(view.render(channel)))
        .orElse(notFound(notFound.render()));
  }

  /**
   * Display an add channel page.
   *
   * @return A page to add a new channel.
   */
  public Result addForm() {
    return ok(add.render(formFactory.form(Channel.class)));
  }

  public Result addSubmit() {
    return TODO;
  }

  /**
   * Display an edit channel page.
   *
   * @param slug The slug of the channel.
   * @return An edit channel page if found else not found page.
   */
  public Result editForm(String slug) {
    return channelRepository
        .findByRoute(slug)
        .map(channel -> ok(
            edit.render(channel.getId(), formFactory.form(Channel.class).fill(channel))
        ))
        .orElse(notFound(notFound.render()));
  }

  public Result editSubmit(long id) {
    return TODO;
  }

  public Result delete(long id) {
    return TODO;
  }
}
