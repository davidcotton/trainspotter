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
   * @param id The ID of the channel.
   * @return A channel page if found.
   */
  public Result view(long id) {
    return channelRepository
        .findById(id)
        .map(channel -> ok(view.render(channel)))
        .orElse(notFound(notFound.render()));
  }

  public Result addForm() {
    return ok(add.render(formFactory.form(Channel.class)));
  }

  public Result addSubmit() {
    return TODO;
  }

  public Result editForm(long id) {
    return channelRepository
        .findById(id)
        .map(channel -> ok(edit.render(id, formFactory.form(Channel.class).fill(channel))))
        .orElse(notFound(notFound.render()));
  }

  public Result editSubmit(long id) {
    return TODO;
  }

  public Result delete(long id) {
    return TODO;
  }
}
