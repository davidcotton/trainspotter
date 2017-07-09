package controllers.app;

import static java.util.Objects.requireNonNull;
import static models.Role.ADMIN;
import static models.Role.CONTRIBUTOR;
import static models.Role.EDITOR;
import static models.Role.SUPER_ADMIN;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import javax.inject.Inject;
import models.create.CreateChannel;
import models.update.UpdateChannel;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import services.ChannelService;
import views.html.channel.add;
import views.html.channel.edit;
import views.html.channel.index;
import views.html.channel.view;
import views.html.notFound;

public class ChannelController extends Controller {

  private final ChannelService channelService;
  private final FormFactory formFactory;

  @Inject
  public ChannelController(ChannelService channelService, FormFactory formFactory) {
    this.channelService = requireNonNull(channelService);
    this.formFactory = requireNonNull(formFactory);
  }

  /**
   * View a paginated list of channels.
   *
   * @param page The page number to load.
   * @return A paginated page of channels.
   */
  public Result index(int page) {
    return ok(index.render(channelService.fetchAllPaged(page)));
  }

  /**
   * View a single channel.
   *
   * @param slug The slug of the channel.
   * @return A channel page if found.
   */
  public Result view(String slug) {
    return channelService
        .findBySlug(slug)
        .map(channel -> ok(view.render(channel)))
        .orElse(notFound(notFound.render()));
  }

  /**
   * Display an add channel page.
   *
   * @return A page to add a new channel.
   */
  @Restrict({@Group(SUPER_ADMIN), @Group(ADMIN), @Group(EDITOR), @Group(CONTRIBUTOR)})
  public Result addForm() {
    return ok(add.render(formFactory.form(CreateChannel.class)));
  }

  /**
   * Process the submission for creating a new Channel.
   *
   * @return Redirect to the new Channel on success else the form with errors.
   */
  @Restrict({@Group(SUPER_ADMIN), @Group(ADMIN), @Group(EDITOR), @Group(CONTRIBUTOR)})
  public Result addSubmit() {
    return channelService
        .insert(formFactory.form(CreateChannel.class).bindFromRequest())
        .fold(
            form -> badRequest(add.render(form)),
            channel -> Results.redirect(routes.ChannelController.view(channel.getSlug()))
        );
  }

  /**
   * Display an edit channel page.
   *
   * @param slug The slug of the channel.
   * @return An edit channel page if found else not found page.
   */
  @Restrict({@Group(SUPER_ADMIN), @Group(ADMIN), @Group(EDITOR), @Group(CONTRIBUTOR)})
  public Result editForm(String slug) {
    return channelService
        .findBySlug(slug)
        .map(channel -> ok(edit.render(
            channel,
            formFactory.form(UpdateChannel.class).fill(new UpdateChannel(channel))
        )))
        .orElse(notFound(notFound.render()));
  }

  /**
   * Process the submission for updating a Channel.
   *
   * @param slug The slug of the channel.
   * @return Redirect to the updated Channel on success else the form with errors.
   */
  @Restrict({@Group(SUPER_ADMIN), @Group(ADMIN), @Group(EDITOR), @Group(CONTRIBUTOR)})
  public Result editSubmit(String slug) {
    return channelService
        .findBySlug(slug)
        .map(savedChannel -> channelService
            .update(savedChannel, formFactory.form(UpdateChannel.class).bindFromRequest())
            .fold(
                form -> badRequest(edit.render(savedChannel, form)),
                channel -> Results.redirect(routes.ChannelController.view(channel.getSlug()))
            )
        )
        .orElse(notFound(notFound.render()));
  }

  /**
   * Delete a Channel.
   *
   * @param slug The slug of the channel.
   * @return Redirects to the Channel list page on success else not found.
   */
  @Restrict({@Group(SUPER_ADMIN), @Group(ADMIN), @Group(EDITOR)})
  public Result delete(String slug) {
    return channelService
        .findBySlug(slug)
        .map(channel -> {
          channelService.delete(channel);
          return Results.redirect(routes.ChannelController.index(1));
        })
        .orElse(notFound(notFound.render()));
  }
}
