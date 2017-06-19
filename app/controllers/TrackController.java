package controllers;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import javax.inject.Inject;
import models.Track;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import repositories.TrackRepository;
import views.html.notFound;
import views.html.track.add;
import views.html.track.edit;
import views.html.track.index;
import views.html.track.view;

public class TrackController extends Controller {

  private final TrackRepository trackRepository;
  private final FormFactory formFactory;

  @Inject
  public TrackController(TrackRepository trackRepository, FormFactory formFactory) {
    this.trackRepository = requireNonNull(trackRepository);
    this.formFactory = requireNonNull(formFactory);
  }

  /**
   * View all tracks.
   *
   * @return A page with all tracks.
   */
  public Result index() {
    return ok(index.render(trackRepository.findAll()));
  }

  /**
   * View a single track.
   *
   * @param id The track's ID.
   * @return A track page if found.
   */
  public Result view(long id) {
    return trackRepository.findById(id)
        .map(track -> ok(view.render(track)))
        .orElse(notFound(notFound.render()));
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
