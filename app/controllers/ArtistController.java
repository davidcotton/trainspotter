package controllers;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import javax.inject.Inject;
import models.Artist;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import repositories.ArtistRepository;
import views.html.artist.add;
import views.html.artist.edit;
import views.html.artist.index;
import views.html.artist.view;

public class ArtistController extends Controller {

  private final ArtistRepository artistRepository;
  private final FormFactory formFactory;

  @Inject
  public ArtistController(ArtistRepository artistRepository, FormFactory formFactory) {
    this.artistRepository = requireNonNull(artistRepository);
    this.formFactory = requireNonNull(formFactory);
  }

  /**
   * View all artists.
   *
   * @return A page with all artists.
   */
  public Result index() {
    return ok(index.render(artistRepository.findAll()));
  }

  /**
   * View a single artist.
   *
   * @param id The artist's ID.
   * @return An artist page if found.
   */
  public Result view(long id) {
    Optional<Artist> maybeArtist = artistRepository.findById(id);
    return ok(view.render(maybeArtist.get()));
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
