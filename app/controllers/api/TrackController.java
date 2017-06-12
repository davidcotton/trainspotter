package controllers.api;

import static java.util.Objects.requireNonNull;

import static play.libs.Json.fromJson;
import static play.libs.Json.toJson;

import static utilities.JsonHelper.MESSAGE_NOT_FOUND;
import static utilities.JsonHelper.errorsAsJson;

import javax.inject.Inject;

import models.Track;

import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import services.TrackService;

public class TrackController extends Controller {

  private final TrackService trackService;

  @Inject
  public TrackController(TrackService trackService) {
    this.trackService = requireNonNull(trackService);
  }

  public Result fetchAll() {
    return ok(toJson(trackService.fetchAll()));
  }

  public Result fetch(long id) {
    return trackService.findById(id)
        .map(track -> ok(toJson(track)))
        .orElse(notFound(errorsAsJson(MESSAGE_NOT_FOUND)));
  }

  @BodyParser.Of(BodyParser.Json.class)
  public Result create() {
    Track track1 = fromJson(request().body().asJson(), Track.class);

    return trackService
        .insert(fromJson(request().body().asJson(), Track.class))
        .fold(
            error -> badRequest(errorsAsJson(error)),
            track -> created(toJson(track))
        );
  }

  @BodyParser.Of(BodyParser.Json.class)
  public Result update(long id) {
    return trackService
        .findById(id)
        .map(savedTrack -> trackService
            .update(savedTrack, fromJson(request().body().asJson(), Track.class))
            .fold(
                error -> badRequest(errorsAsJson(error)),
                newTrack -> created(toJson(newTrack))
            )
        )
        .orElse(notFound(errorsAsJson(MESSAGE_NOT_FOUND)));
  }

  public Result delete(long id) {
    return TODO;
  }
}
