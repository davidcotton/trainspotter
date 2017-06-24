package controllers;

import static java.util.Objects.requireNonNull;

import javax.inject.Inject;
import models.Tracklist;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import repositories.TracklistRepository;
import views.html.notFound;
import views.html.tracklist.add;
import views.html.tracklist.edit;
import views.html.tracklist.index;
import views.html.tracklist.view;

public class TracklistController extends Controller {

  private final TracklistRepository tracklistRepository;
  private final FormFactory formFactory;

  @Inject
  public TracklistController(TracklistRepository tracklistRepository, FormFactory formFactory) {
    this.tracklistRepository = requireNonNull(tracklistRepository);
    this.formFactory = requireNonNull(formFactory);
  }

  /**
   * View all tracklists.
   *
   * @return A page with all tracklists.
   */
  public Result index(int page) {
    return ok(index.render(tracklistRepository.findAllPaged(page)));
  }

  /**
   * View a single tracklist.
   *
   * @param id The tracklist's ID.
   * @return A tracklist page if found.
   */
  public Result view(long id) {
    return tracklistRepository
        .findById(id)
        .map(tracklist -> ok(view.render(tracklist)))
        .orElse(notFound(notFound.render()));
  }

  public Result addForm() {
    return ok(add.render(formFactory.form(Tracklist.class)));
  }

  public Result addSubmit() {
    return TODO;
  }

  public Result editForm(long id) {
    return tracklistRepository
        .findById(id)
        .map(tracklist -> ok(edit.render(id, formFactory.form(Tracklist.class).fill(tracklist))))
        .orElse(notFound(notFound.render()));
  }

  public Result editSubmit(long id) {
    return TODO;
  }

  public Result delete(long id) {
    return TODO;
  }
}
