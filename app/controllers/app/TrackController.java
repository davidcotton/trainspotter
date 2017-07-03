package controllers.app;

import static java.util.Objects.requireNonNull;
import static models.Role.ADMIN;
import static models.Role.CONTRIBUTOR;
import static models.Role.EDITOR;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import javax.inject.Inject;
import models.create.CreateTrack;
import models.update.UpdateTrack;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
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
  @Restrict({@Group(ADMIN), @Group(EDITOR), @Group(CONTRIBUTOR)})
  public Result addForm() {
    return ok(add.render(formFactory.form(CreateTrack.class)));
  }

  @Restrict({@Group(ADMIN), @Group(EDITOR), @Group(CONTRIBUTOR)})
  public Result addSubmit() {
    return TODO;
  }

  @Restrict({@Group(ADMIN), @Group(EDITOR), @Group(CONTRIBUTOR)})
  public Result editForm(long id) {
    return trackService
        .findById(id)
        .map(track -> ok(edit.render(
            track,
            formFactory.form(UpdateTrack.class).fill(new UpdateTrack(track))
        )))
        .orElse(notFound(notFound.render()));
  }

  @Restrict({@Group(ADMIN), @Group(EDITOR), @Group(CONTRIBUTOR)})
  public Result editSubmit(long id) {
    return TODO;
  }

  /**
   * Delete a Track.
   *
   * @param id The ID of the track.
   * @return Redirects to the Track list page on success, else not found.
   */
  @Restrict({@Group(ADMIN)})
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
