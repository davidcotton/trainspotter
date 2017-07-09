package controllers.app;

import static java.util.Objects.requireNonNull;
import static models.Role.ADMIN;
import static models.Role.CONTRIBUTOR;
import static models.Role.EDITOR;
import static models.Role.SUPER_ADMIN;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import java.util.List;
import javax.inject.Inject;
import models.Genre;
import models.Label;
import models.create.CreateTrack;
import models.update.UpdateTrack;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import services.GenreService;
import services.LabelService;
import services.TrackService;
import views.html.notFound;
import views.html.track.add;
import views.html.track.edit;
import views.html.track.index;
import views.html.track.view;

public class TrackController extends Controller {

  private final TrackService trackService;
  private final FormFactory formFactory;
  private final GenreService genreService;
  private final LabelService labelService;

  @Inject
  public TrackController(
      TrackService trackService,
      FormFactory formFactory,
      GenreService genreService,
      LabelService labelService
  ) {
    this.trackService = requireNonNull(trackService);
    this.formFactory = requireNonNull(formFactory);
    this.genreService = requireNonNull(genreService);
    this.labelService = requireNonNull(labelService);
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
  @Restrict({@Group(SUPER_ADMIN), @Group(ADMIN), @Group(EDITOR), @Group(CONTRIBUTOR)})
  public Result addForm() {
    List<Genre> genres = genreService.fetchAll();
    List<Label> labels = labelService.fetchAll();

    return ok(add.render(formFactory.form(CreateTrack.class), genres, labels));
  }

  @Restrict({@Group(SUPER_ADMIN), @Group(ADMIN), @Group(EDITOR), @Group(CONTRIBUTOR)})
  public Result addSubmit() {
    List<Genre> genres = genreService.fetchAll();
    List<Label> labels = labelService.fetchAll();

    return trackService
        .insert(formFactory.form(CreateTrack.class).bindFromRequest())
        .fold(
            form -> badRequest(add.render(form, genres, labels)),
            track -> Results.redirect(routes.TrackController.view(track.getId()))
        );
  }

  @Restrict({@Group(SUPER_ADMIN), @Group(ADMIN), @Group(EDITOR), @Group(CONTRIBUTOR)})
  public Result editForm(long id) {
    List<Genre> genres = genreService.fetchAll();
    List<Label> labels = labelService.fetchAll();

    return trackService
        .findById(id)
        .map(track -> ok(edit.render(
            track,
            formFactory.form(UpdateTrack.class).fill(new UpdateTrack(track)),
            genres,
            labels
        )))
        .orElse(notFound(notFound.render()));
  }

  @Restrict({@Group(SUPER_ADMIN), @Group(ADMIN), @Group(EDITOR), @Group(CONTRIBUTOR)})
  public Result editSubmit(long id) {
    List<Genre> genres = genreService.fetchAll();
    List<Label> labels = labelService.fetchAll();

    return trackService
        .findById(id)
        .map(savedTrack -> trackService
            .update(savedTrack, formFactory.form(UpdateTrack.class).bindFromRequest())
            .fold(
                form -> badRequest(edit.render(savedTrack, form, genres, labels)),
                track -> Results.redirect(routes.TrackController.view(track.getId()))
            )
        )
        .orElse(notFound(notFound.render()));
  }

  /**
   * Delete a Track.
   *
   * @param id The ID of the track.
   * @return Redirects to the Track list page on success, else not found.
   */
  @Restrict({@Group(SUPER_ADMIN), @Group(ADMIN), @Group(EDITOR)})
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
