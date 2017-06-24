package controllers;

import static java.util.Objects.requireNonNull;

import javax.inject.Inject;
import models.Channel;
import play.data.FormFactory;
import play.mvc.*;
import play.mvc.Security;
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
   * View a paginated list of channels.
   *
   * @param page The page number to load.
   * @return A paginated page of channels.
   */
  public Result index(int page) {
    return ok(index.render(channelRepository.findAllPaged(page)));
  }

  /**
   * View a single channel.
   *
   * @param slug The slug of the channel.
   * @return A channel page if found.
   */
  public Result view(String slug) {
    return channelRepository
        .findBySlug(slug)
        .map(channel -> ok(view.render(channel)))
        .orElse(notFound(notFound.render()));
  }

  /**
   * Display an add channel page.
   *
   * @return A page to add a new channel.
   */
  @play.mvc.Security.Authenticated(Secured.class)
  public Result addForm() {
    return ok(add.render(formFactory.form(Channel.class)));
  }

  @Security.Authenticated(Secured.class)
  public Result addSubmit() {
    return TODO;
  }

  /**
   * Display an edit channel page.
   *
   * @param slug The slug of the channel.
   * @return An edit channel page if found else not found page.
   */
  @Security.Authenticated(Secured.class)
  public Result editForm(String slug) {
    return channelRepository
        .findBySlug(slug)
        .map(channel -> ok(
            edit.render(channel, formFactory.form(Channel.class).fill(channel))
        ))
        .orElse(notFound(notFound.render()));
  }

  @Security.Authenticated(Secured.class)
  public Result editSubmit(String slug) {
    return TODO;
  }

  @Security.Authenticated(Secured.class)
  public Result delete(String slug) {
    return TODO;
  }
}
