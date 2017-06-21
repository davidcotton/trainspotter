package controllers;

import static java.util.Objects.requireNonNull;

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
   * @param slug The genre's ID.
   * @return A genre page if found.
   */
  public Result view(String slug) {
    return genreRepository
        .findBySlug(slug)
        .map(genre -> ok(view.render(genre)))
        .orElse(notFound(notFound.render()));
  }

  public Result addForm() {
    return ok(add.render(formFactory.form(Genre.class)));
  }

  public Result addSubmit() {
    return TODO;
  }

  public Result editForm(String slug) {
    return genreRepository
        .findBySlug(slug)
        .map(genre -> ok(edit.render(genre, formFactory.form(Genre.class).fill(genre))))
        .orElse(notFound(notFound.render()));
  }

  public Result editSubmit(String slug) {
    return TODO;
  }

  public Result delete(String slug) {
    return TODO;
  }
}
