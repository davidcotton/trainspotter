package controllers;

import static java.util.Objects.requireNonNull;
import static play.libs.Json.fromJson;
import static play.libs.Json.toJson;
import static utils.JsonHelper.MESSAGE_NOT_FOUND;
import static utils.JsonHelper.errorsAsJson;

import javax.inject.Inject;
import models.Track;
import play.mvc.Controller;
import play.mvc.Result;
import services.TrackService;

public class TrackController extends Controller {

  private final TrackService trackService;

  @Inject
  public TrackController(TrackService trackService) {
    this.trackService = requireNonNull(trackService);
  }

  public Result index() {
    return ok(toJson(trackService.findAll()));
  }

  public Result view(long id) {
    return trackService.findById(id)
        .map(track -> ok(toJson(track)))
        .orElse(notFound(errorsAsJson(MESSAGE_NOT_FOUND)));
  }

  public Result update(long id) {
    return TODO;
  }

  public Result create() {
    return trackService
        .insert(fromJson(request().body().asJson(), Track.class))
        .fold(
            error -> badRequest(errorsAsJson(error)),
            track -> created(toJson(track))
        );
  }

  public Result delete(long id) {
    return TODO;
  }
}
