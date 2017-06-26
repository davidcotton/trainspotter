package controllers;

import static java.util.Objects.requireNonNull;

import javax.inject.Inject;
import models.Genre;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import repositories.GenreRepository;
import repositories.TracklistRepository;
import views.html.genre.add;
import views.html.genre.edit;
import views.html.genre.index;
import views.html.genre.view;
import views.html.notFound;

public class GenreController extends Controller {

  private final GenreRepository genreRepository;
  private final FormFactory formFactory;
  private final TracklistRepository tracklistRepository;

  @Inject
  public GenreController(
      GenreRepository genreRepository,
      FormFactory formFactory,
      TracklistRepository tracklistRepository
  ) {
    this.genreRepository = requireNonNull(genreRepository);
    this.formFactory = requireNonNull(formFactory);
    this.tracklistRepository = requireNonNull(tracklistRepository);
  }

  /**
   * View all genres.
   *
   * @return A page with all genres.
   */
  public Result index() {
    return ok(index.render(genreRepository.findAll()));
  }

  /**
   * View a genre and tracklist of that genre.
   *
   * @param slug The genre's slug.
   * @param page The paginator page number.
   * @return A genre page if found.
   */
  public Result view(String slug, int page) throws Exception {
    return genreRepository
        .findBySlug(slug)
        .map(
            genre -> ok(
                view.render(genre, tracklistRepository.findAllPagedByGenre(genre, page))
            )
        )
        .orElse(notFound(notFound.render()));
  }

  @play.mvc.Security.Authenticated(Secured.class)
  public Result addForm() {
    return ok(add.render(formFactory.form(Genre.class)));
  }

  @Security.Authenticated(Secured.class)
  public Result addSubmit() {
    return TODO;
  }

  @Security.Authenticated(Secured.class)
  public Result editForm(String slug) {
    return genreRepository
        .findBySlug(slug)
        .map(genre -> ok(edit.render(genre, formFactory.form(Genre.class).fill(genre))))
        .orElse(notFound(notFound.render()));
  }

  @Security.Authenticated(Secured.class)
  public Result editSubmit(String slug) {
    return TODO;
  }

  @Security.Authenticated(Secured.class)
  public Result delete(String slug) {
    return TODO;
  }
}
