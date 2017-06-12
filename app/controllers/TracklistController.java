package controllers;

import static java.util.Objects.requireNonNull;
import static play.libs.Json.fromJson;
import static play.libs.Json.toJson;
import static utilities.JsonHelper.errorsAsJson;

import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import models.Tracklist;
import play.mvc.Controller;
import play.mvc.Result;
import services.TracklistService;
import views.html.tracklist.index;
import views.html.tracklist.view;

public class TracklistController extends Controller {

  private final TracklistService tracklistService;

  @Inject
  public TracklistController(TracklistService tracklistService) {
    this.tracklistService = requireNonNull(tracklistService);
  }

  public Result index() {
    List<Tracklist> tracklists = tracklistService.fetchAll();
    return ok(index.render(tracklists));
  }

  public Result view(long id) {
    Optional<Tracklist> maybeTracklist = tracklistService.findById(id);
    return ok(view.render(maybeTracklist.get()));
  }

  public Result update(long id) {
    return TODO;
  }

  public Result create() {
    return tracklistService
        .insert(fromJson(request().body().asJson(), Tracklist.class))
        .fold(
            error -> badRequest(errorsAsJson(error)),
            tracklist -> created(toJson(tracklist))
        );
  }

  public Result delete(long id) {
    return TODO;
  }
}
