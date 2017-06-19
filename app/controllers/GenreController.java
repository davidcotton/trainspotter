package controllers;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import javax.inject.Inject;
import models.Genre;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import repositories.GenreRepository;
import views.html.genre.add;
import views.html.genre.edit;
import views.html.genre.index;
import views.html.genre.view;
import views.html.notFound;

public class GenreController extends Controller {

  private final GenreRepository genreRepository;
  private final FormFactory formFactory;

  @Inject
  public GenreController(GenreRepository genreRepository, FormFactory formFactory) {
    this.genreRepository = requireNonNull(genreRepository);
    this.formFactory = requireNonNull(formFactory);
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
   * View a single genre.
   *
   * @param id The genre's ID.
   * @return A genre page if found.
   */
  public Result view(long id) {
    return genreRepository
        .findById(id)
        .map(genre -> ok(view.render(genre)))
        .orElse(notFound(notFound.render()));
  }

  public Result addForm() {
    return TODO;
  }

  public Result addSubmit() {
    return TODO;
  }

  public Result editForm(long id) {
    return TODO;
  }

  public Result editSubmit(long id) {
    return TODO;
  }

  public Result delete(long id) {
    return TODO;
  }
}
