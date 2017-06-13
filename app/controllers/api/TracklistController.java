package controllers.api;

import com.fasterxml.jackson.databind.JsonNode;

import static java.util.Objects.requireNonNull;

import static play.libs.Json.fromJson;
import static play.libs.Json.toJson;

import static utilities.JsonHelper.MESSAGE_NOT_FOUND;
import static utilities.JsonHelper.errorsAsJson;

import javax.inject.Inject;

import models.Tracklist;

import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import services.TracklistService;

public class TracklistController extends Controller {

  private final TracklistService tracklistService;

  @Inject
  public TracklistController(TracklistService tracklistService) {
    this.tracklistService = requireNonNull(tracklistService);
  }

  public Result fetchAll() {
    return ok(toJson(tracklistService.fetchAll()));
  }

  public Result fetch(long id) {
    return tracklistService.findById(id)
        .map(tracklist -> ok(toJson(tracklist)))
        .orElse(notFound(errorsAsJson(MESSAGE_NOT_FOUND)));
  }

  @BodyParser.Of(BodyParser.Json.class)
  public Result create() {
    JsonNode jsonNode = request().body().asJson();
    Tracklist tracklist1 = fromJson(jsonNode, Tracklist.class);
    return tracklistService
        .insert(tracklist1)
        .fold(
            error -> badRequest(errorsAsJson(error)),
            tracklist -> created(toJson(tracklist))
        );
  }

  @BodyParser.Of(BodyParser.Json.class)
  public Result update(long id) {
    return tracklistService
        .findById(id)
        .map(savedTracklist -> tracklistService
            .update(savedTracklist, fromJson(request().body().asJson(), Tracklist.class))
            .fold(
                error -> badRequest(errorsAsJson(error)),
                newTracklist -> created(toJson(newTracklist))
            )
        )
        .orElse(notFound(errorsAsJson(MESSAGE_NOT_FOUND)));
  }

  public Result delete(long id) {
    return TODO;
  }
}
