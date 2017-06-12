package controllers;

import static java.util.Objects.requireNonNull;
import static play.libs.Json.fromJson;
import static play.libs.Json.toJson;
import static utilities.JsonHelper.errorsAsJson;

import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import models.Track;
import play.mvc.Controller;
import play.mvc.Result;
import services.TrackService;
import views.html.track.index;
import views.html.track.view;

public class TrackController extends Controller {

  private final TrackService trackService;

  @Inject
  public TrackController(TrackService trackService) {
    this.trackService = requireNonNull(trackService);
  }

  public Result index() {
    List<Track> tracks = trackService.fetchAll();
    return ok(index.render(tracks));
  }

  public Result view(long id) {
    Optional<Track> maybeTrack = trackService.findById(id);
    return ok(view.render(maybeTrack.get()));
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
