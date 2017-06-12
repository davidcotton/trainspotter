package controllers;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import javax.inject.Inject;
import models.Tracklist;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import repositories.TracklistRepository;
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
  public Result index() {
    return ok(index.render(tracklistRepository.findAll()));
  }

  /**
   * View a single tracklist.
   *
   * @param id The tracklist's ID.
   * @return A tracklist page if found.
   */
  public Result view(long id) {
    Optional<Tracklist> maybeTracklist = tracklistRepository.findById(id);
    return ok(view.render(maybeTracklist.get()));
  }

  public Result add() {
    return TODO;
  }

  public Result edit(long id) {
    return TODO;
  }

  public Result delete(long id) {
    return TODO;
  }
}
