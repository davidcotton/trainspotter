package controllers.app;

import javax.inject.Inject;
import play.mvc.Controller;
import play.mvc.Result;
import services.TracklistService;
import views.html.index;

import static java.util.Objects.requireNonNull;

public class ApplicationController extends Controller {

  private final TracklistService tracklistService;

  @Inject
  public ApplicationController(TracklistService tracklistService) {
    this.tracklistService = requireNonNull(tracklistService);
  }

  /**
   * View the homepage.
   *
   * @return The homepage.
   */
  public Result index() {
    return ok(index.render(tracklistService.findMostPopular()));
  }
}
