package controllers.api;

import static java.util.Objects.requireNonNull;
import static play.libs.Json.fromJson;
import static play.libs.Json.toJson;
import static utilities.JsonHelper.MESSAGE_NOT_FOUND;
import static utilities.JsonHelper.errorsAsJson;

import javax.inject.Inject;
import models.Channel;
import models.CreateChannel;
import models.UpdateChannel;
import play.data.FormFactory;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import services.ChannelService;

public class ChannelController extends Controller {

  private final ChannelService channelService;
  private final FormFactory formFactory;

  @Inject
  public ChannelController(ChannelService channelService, FormFactory formFactory) {
    this.channelService = requireNonNull(channelService);
    this.formFactory = requireNonNull(formFactory);
  }

  /**
   * Fetch all Channels.
   *
   * @return A collection of Channels.
   */
  public Result fetchAll() {
    return ok(toJson(channelService.fetchAll()));
  }

  /**
   * Fetch a single Channel.
   *
   * @param id The ID of the Channel to fetch.
   * @return The Channel if found, else the error.
   */
  public Result fetch(long id) {
    return channelService.findById(id)
        .map(channel -> ok(toJson(channel)))
        .orElse(notFound(errorsAsJson(MESSAGE_NOT_FOUND)));
  }

  /**
   * Create a new Channel.
   *
   * @return The new Channel on success, else the errors.
   */
  @BodyParser.Of(BodyParser.Json.class)
  public Result create() {
    return channelService
        .insert(formFactory.form(CreateChannel.class).bindFromRequest())
        .fold(
            form -> badRequest(errorsAsJson(form.errors())),
            channel -> created(toJson(channel))
        );
  }

  /**
   * Update a Channel.
   *
   * @param id The ID of the Channel to update.
   * @return The updated Channel on success, else the errors.
   */
  @BodyParser.Of(BodyParser.Json.class)
  public Result update(long id) {
    return channelService
        .findById(id)
        .map(savedChannel -> channelService
            .update(savedChannel, formFactory.form(UpdateChannel.class).bindFromRequest())
            .fold(
                form -> badRequest(errorsAsJson(form.errors())),
                newChannel -> created(toJson(newChannel))
            )
        )
        .orElse(notFound(errorsAsJson(MESSAGE_NOT_FOUND)));
  }

  /**
   * Delete a Channel.
   *
   * @param id The ID of the Channel to delete.
   * @return HTTP 204 No Content on success, else the errors.
   */
  public Result delete(long id) {
    return channelService
        .findById(id)
        .map(channel -> {
          channelService.delete(channel);
          return (Result) noContent();
        })
        .orElse(notFound(errorsAsJson(MESSAGE_NOT_FOUND)));
  }
}
