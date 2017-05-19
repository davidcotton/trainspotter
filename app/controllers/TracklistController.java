package controllers;

import static java.util.Objects.requireNonNull;
import static play.libs.Json.fromJson;
import static play.libs.Json.toJson;
import static utils.JsonHelper.errorsAsJson;

import javax.inject.Inject;
import models.Tracklist;
import play.mvc.Controller;
import play.mvc.Result;
import services.TracklistService;

public class TracklistController extends Controller {

  private final TracklistService tracklistService;

  @Inject
  public TracklistController(TracklistService tracklistService) {
    this.tracklistService = requireNonNull(tracklistService);
  }

  public Result findAll() {
    return ok(toJson(tracklistService.findAll()));
  }

  public Result find(long id) {
    return tracklistService.findById(id)
        .map(user -> ok(toJson(user)))
        .orElse(notFound(toJson("Not found")));
  }

  public Result create() {
    return tracklistService
        .insert(fromJson(request().body().asJson(), Tracklist.class))
        .fold(
            error -> badRequest(errorsAsJson(error)),
            tracklist -> created(toJson(tracklist))
        );
  }

  public Result update(long id) {
    return TODO;
  }

  public Result delete(long id) {
    return TODO;
  }
}
