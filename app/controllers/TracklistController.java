package controllers;

import static java.util.Objects.requireNonNull;
import static play.libs.Json.fromJson;
import static play.libs.Json.toJson;
import static utilities.JsonHelper.errorsAsJson;

import java.util.Optional;
import javax.inject.Inject;
import models.Tracklist;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import services.TracklistService;
import views.html.tracklist.index;
import views.html.tracklist.view;

public class TracklistController extends Controller {

  private final TracklistService tracklistService;
  private final FormFactory formFactory;

  @Inject
  public TracklistController(TracklistService tracklistService, FormFactory formFactory) {
    this.tracklistService = requireNonNull(tracklistService);
    this.formFactory = requireNonNull(formFactory);
  }

  public Result index() {
    return ok(index.render(tracklistService.fetchAll()));
  }

  public Result view(long id) {
    Optional<Tracklist> maybeTracklist = tracklistService.findById(id);
    return ok(view.render(maybeTracklist.get()));
  }

  public Result add() {
    return tracklistService
        .insert(fromJson(request().body().asJson(), Tracklist.class))
        .fold(
            error -> badRequest(errorsAsJson(error)),
            tracklist -> created(toJson(tracklist))
        );
  }

  public Result edit(long id) {
    return TODO;
  }

  public Result delete(long id) {
    return TODO;
  }
}
