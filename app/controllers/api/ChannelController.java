package controllers.api;

import static java.util.Objects.requireNonNull;
import static play.libs.Json.fromJson;
import static play.libs.Json.toJson;
import static utilities.JsonHelper.MESSAGE_NOT_FOUND;
import static utilities.JsonHelper.errorsAsJson;

import javax.inject.Inject;
import models.Channel;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import services.ChannelService;

public class ChannelController extends Controller {

  private final ChannelService channelService;

  @Inject
  public ChannelController(ChannelService channelService) {
    this.channelService = requireNonNull(channelService);
  }

  public Result fetchAll() {
    return ok(toJson(channelService.fetchAll()));
  }

  public Result fetch(long id) {
    return channelService.findById(id)
        .map(channel -> ok(toJson(channel)))
        .orElse(notFound(errorsAsJson(MESSAGE_NOT_FOUND)));
  }

  @BodyParser.Of(BodyParser.Json.class)
  public Result create() {
    return channelService
        .insert(fromJson(request().body().asJson(), Channel.class))
        .fold(
            error -> badRequest(errorsAsJson(error)),
            channel -> created(toJson(channel))
        );
  }

  @BodyParser.Of(BodyParser.Json.class)
  public Result update(long id) {
    return channelService
        .findById(id)
        .map(savedChannel -> channelService
            .update(savedChannel, fromJson(request().body().asJson(), Channel.class))
            .fold(
                error -> badRequest(errorsAsJson(error)),
                newChannel -> created(toJson(newChannel))
            )
        )
        .orElse(notFound(errorsAsJson(MESSAGE_NOT_FOUND)));
  }

  public Result delete(long id) {
    return TODO;
  }
}
