package controllers;

import static java.util.Objects.requireNonNull;
import static play.libs.Json.toJson;
import static play.mvc.Results.TODO;
import static play.mvc.Results.notFound;
import static play.mvc.Results.ok;

import javax.inject.Inject;
import play.mvc.Result;
import services.TrackService;

public class TrackController {

  private final TrackService trackService;

  @Inject
  public TrackController(TrackService trackService) {
    this.trackService = requireNonNull(trackService);
  }

  public Result findAll() {
    return ok(toJson(trackService.findAll()));
  }

  public Result find(long id) {
    return trackService.findById(id)
        .map(track -> ok(toJson(track)))
        .orElse(notFound(toJson("Not found")));
  }

  public Result create() {
    return TODO;
  }

  public Result update(long id) {
    return TODO;
  }

  public Result delete(long id) {
    return TODO;
  }
}
