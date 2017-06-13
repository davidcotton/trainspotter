package controllers;

import javax.inject.Inject;

import play.mvc.Controller;
import play.mvc.Result;
import repositories.TracklistRepository;
import views.html.index;

import static java.util.Objects.requireNonNull;

public class ApplicationController extends Controller {

  private final TracklistRepository tracklistRepository;

  @Inject
  public ApplicationController(TracklistRepository tracklistRepository) {
    this.tracklistRepository = requireNonNull(tracklistRepository);
  }

  public Result index() {
    return ok(index.render(tracklistRepository.findAll()));
  }

}
