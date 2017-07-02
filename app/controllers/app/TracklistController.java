package controllers.app;

import static java.util.Objects.requireNonNull;

import javax.inject.Inject;
import models.create.CreateTracklist;
import models.update.UpdateTracklist;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.Security;
import security.SessionAuthenticator;
import services.TracklistService;
import views.html.notFound;
import views.html.tracklist.add;
import views.html.tracklist.edit;
import views.html.tracklist.index;
import views.html.tracklist.view;

public class TracklistController extends Controller {

  private final TracklistService tracklistService;
  private final FormFactory formFactory;

  @Inject
  public TracklistController(TracklistService tracklistRepository, FormFactory formFactory) {
    this.tracklistService = requireNonNull(tracklistRepository);
    this.formFactory = requireNonNull(formFactory);
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

  @Security.Authenticated(SessionAuthenticator.class)
  public Result addForm() {
    return ok(add.render(formFactory.form(CreateTracklist.class)));
  }

  @Security.Authenticated(SessionAuthenticator.class)
  public Result addSubmit() {
    return tracklistService
        .insert(formFactory.form(CreateTracklist.class).bindFromRequest())
        .fold(
            form -> badRequest(add.render(form)),
            tracklist -> Results.redirect(routes.TracklistController.view(tracklist.getId()))
        );
  }

  @Security.Authenticated(SessionAuthenticator.class)
  public Result editForm(long id) {
    return tracklistService
        .findById(id)
        .map(tracklist -> ok(edit.render(
            tracklist,
            formFactory.form(UpdateTracklist.class).fill(new UpdateTracklist(tracklist))
        )))
        .orElse(notFound(notFound.render()));
  }

  @Security.Authenticated(SessionAuthenticator.class)
  public Result editSubmit(long id) {
    return tracklistService
        .findById(id)
        .map(savedTracklist -> tracklistService
            .update(savedTracklist, formFactory.form(UpdateTracklist.class).bindFromRequest())
            .fold(
                form -> badRequest(edit.render(savedTracklist, form)),
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
  @Security.Authenticated(SessionAuthenticator.class)
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
