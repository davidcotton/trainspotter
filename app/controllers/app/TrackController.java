package controllers.app;

import static java.util.Objects.requireNonNull;

import javax.inject.Inject;
import models.create.CreateTrack;
import models.update.UpdateTrack;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.Security;
import services.TrackService;
import views.html.notFound;
import views.html.track.add;
import views.html.track.edit;
import views.html.track.index;
import views.html.track.view;

public class TrackController extends Controller {

  private final TrackService trackService;
  private final FormFactory formFactory;

  @Inject
  public TrackController(TrackService trackService, FormFactory formFactory) {
    this.trackService = requireNonNull(trackService);
    this.formFactory = requireNonNull(formFactory);
  }

  /**
   * View all tracks.
   *
   * @return A page with all tracks.
   */
  public Result index() {
    return ok(index.render(trackService.fetchAll()));
  }

  /**
   * View a single track.
   *
   * @param id The ID of the track.
   * @return A track page if found.
   */
  public Result view(long id) {
    return trackService
        .findById(id)
        .map(track -> ok(view.render(track)))
        .orElse(notFound(notFound.render()));
  }

  /**
   * Display an add Track page.
   *
   * @return A page allowing the user to add a Track.
   */
  @Security.Authenticated(Secured.class)
  public Result addForm() {
    return ok(add.render(formFactory.form(CreateTrack.class)));
  }

  @Security.Authenticated(Secured.class)
  public Result addSubmit() {
    return TODO;
  }

  @Security.Authenticated(Secured.class)
  public Result editForm(long id) {
    return trackService
        .findById(id)
        .map(track -> ok(edit.render(
            track,
            formFactory.form(UpdateTrack.class).fill(new UpdateTrack(track))
        )))
        .orElse(notFound(notFound.render()));
  }

  @Security.Authenticated(Secured.class)
  public Result editSubmit(long id) {
    return TODO;
  }

  /**
   * Delete a Track.
   *
   * @param id The ID of the track.
   * @return Redirects to the Track index page on success, else not found.
   */
  @Security.Authenticated(Secured.class)
  public Result delete(long id) {
    return trackService
        .findById(id)
        .map(track -> {
          trackService.delete(track);
          return Results.redirect(routes.TrackController.index());
        })
        .orElse(notFound(notFound.render()));
  }
}
