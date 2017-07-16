package controllers.app;

import static java.util.Objects.requireNonNull;
import static models.Role.ADMIN;
import static models.Role.CONTRIBUTOR;
import static models.Role.EDITOR;
import static models.Role.SUPER_ADMIN;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import javax.inject.Inject;
import models.create.CreateTracklist;
import models.update.UpdateTracklist;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import services.ArtistService;
import services.TracklistService;
import services.UserService;
import views.html.notFound;
import views.html.tracklist.add;
import views.html.tracklist.edit;
import views.html.tracklist.index;
import views.html.tracklist.view;

public class TracklistController extends Controller {

  private final TracklistService tracklistService;
  private final FormFactory formFactory;
  private final UserService userService;
  private final ArtistService artistService;

  @Inject
  public TracklistController(
      TracklistService tracklistRepository,
      FormFactory formFactory,
      UserService userService,
      ArtistService artistService
  ) {
    this.tracklistService = requireNonNull(tracklistRepository);
    this.formFactory = requireNonNull(formFactory);
    this.userService = requireNonNull(userService);
    this.artistService = requireNonNull(artistService);
  }

  /**
   * View all tracklists.
   *
   * @return A page with all tracklists.
   */
  public Result index(int page) {
    return ok(index.render(tracklistService.fetchAllPaged(page)));
  }

  /**
   * View a single tracklist.
   *
   * @param id The ID of the Tracklist.
   * @return A tracklist page if found.
   */
  public Result view(long id) {
    return tracklistService
        .findById(id)
        .map(tracklist -> ok(view.render(tracklist)))
        .orElse(notFound(notFound.render()));
  }

  @Restrict({@Group(SUPER_ADMIN), @Group(ADMIN), @Group(EDITOR), @Group(CONTRIBUTOR)})
  public Result addForm() {
    return ok(add.render(formFactory.form(CreateTracklist.class), artistService.fetchAll()));
  }

  @Restrict({@Group(SUPER_ADMIN), @Group(ADMIN), @Group(EDITOR), @Group(CONTRIBUTOR)})
  public Result addSubmit() {
    return userService
        .findBySlug(session("userslug"))
        .map(user -> tracklistService
            .insert(formFactory.form(CreateTracklist.class).bindFromRequest(), user)
            .fold(
                form -> badRequest(add.render(form, artistService.fetchAll())),
                tracklist -> Results.redirect(routes.TracklistController.view(tracklist.getId()))
            )
        )
        .orElse(Results.redirect(routes.ApplicationController.index()));

  }

  @Restrict({@Group(SUPER_ADMIN), @Group(ADMIN), @Group(EDITOR), @Group(CONTRIBUTOR)})
  public Result editForm(long id) {
    return tracklistService
        .findById(id)
        .map(tracklist -> ok(edit.render(
            tracklist,
            formFactory.form(UpdateTracklist.class).fill(new UpdateTracklist(tracklist)),
            artistService.fetchAll()
        )))
        .orElse(notFound(notFound.render()));
  }

  @Restrict({@Group(SUPER_ADMIN), @Group(ADMIN), @Group(EDITOR), @Group(CONTRIBUTOR)})
  public Result editSubmit(long id) {
    return tracklistService
        .findById(id)
        .map(savedTracklist -> tracklistService
            .update(savedTracklist, formFactory.form(UpdateTracklist.class).bindFromRequest())
            .fold(
                form -> badRequest(edit.render(savedTracklist, form, artistService.fetchAll())),
                newTracklist -> Results.redirect(routes.TracklistController.view(newTracklist.getId()))
            )
        )
        .orElse(notFound(notFound.render()));
  }

  /**
   * Delete a Tracklist.
   *
   * @param id The ID of the Tracklist.
   * @return Redirects to the Tracklist list page on success, else not found.
   */
  @Restrict({@Group(SUPER_ADMIN), @Group(ADMIN), @Group(EDITOR)})
  public Result delete(long id) {
    return tracklistService
        .findById(id)
        .map(tracklist -> {
          tracklistService.delete(tracklist);
          return Results.redirect(routes.TracklistController.index(1));
        })
        .orElse(notFound(notFound.render()));
  }
}
