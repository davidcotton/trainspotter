package controllers;

import static java.util.Objects.requireNonNull;

import javax.inject.Inject;
import models.Artist;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import repositories.ArtistRepository;
import repositories.TracklistRepository;
import views.html.artist.add;
import views.html.artist.edit;
import views.html.artist.index;
import views.html.artist.tracklists;
import views.html.artist.view;
import views.html.notFound;

public class ArtistController extends Controller {

  private final ArtistRepository artistRepository;
  private final FormFactory formFactory;
  private final TracklistRepository tracklistRepository;

  @Inject
  public ArtistController(
      ArtistRepository artistRepository,
      FormFactory formFactory,
      TracklistRepository tracklistRepository
  ) {
    this.artistRepository = requireNonNull(artistRepository);
    this.formFactory = requireNonNull(formFactory);
    this.tracklistRepository = requireNonNull(tracklistRepository);
  }

  /**
   * View all artists.
   *
   * @return A page with all artists.
   */
  public Result index(int page) {
    return ok(index.render(artistRepository.findAllPaged(--page)));
  }

  /**
   * View a single artist.
   *
   * @param id The artist's ID.
   * @return An artist page if found.
   */
  public Result view(long id) {
    return artistRepository
        .findById(id)
        .map(artist -> ok(view.render(artist)))
        .orElse(notFound(notFound.render()));
  }

  /**
   * Display an add artist page.
   *
   * @return A page allowing the user to add an artist.
   */
  public Result addForm() {
    return ok(add.render(formFactory.form(Artist.class)));
  }

  public Result addSubmit() {
    return TODO;
  }

  /**
   * Display an edit artist page.
   *
   * @param id The ID of the artist.
   * @return An edit artist page if artist is found else not found page.
   */
  public Result editForm(long id) {
    return artistRepository
        .findById(id)
        .map(artist -> ok(edit.render(id, formFactory.form(Artist.class).fill(artist))))
        .orElse(notFound(notFound.render()));
  }

  public Result editSubmit(long id) {
    return TODO;
  }

  public Result delete(long id) {
    return TODO;
  }

  public Result tracklists(long id, int page) {
    return artistRepository
        .findById(id)
        .map(
            artist -> ok(
                tracklists.render(artist, tracklistRepository.findAllPagedByArtist(artist, page))
            )
        )
        .orElse(notFound(notFound.render()));
  }
}
