package controllers;

import static java.util.Objects.requireNonNull;
import static play.libs.Json.toJson;
import static play.mvc.Results.TODO;
import static play.mvc.Results.notFound;
import static play.mvc.Results.ok;

import javax.inject.Inject;
import play.mvc.Result;
import services.TracklistService;

public class TracklistController {

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
    return TODO;
  }

  public Result update(long id) {
    return TODO;
  }

  public Result delete(long id) {
    return TODO;
  }
}
