package controllers;

import java.util.Optional;

import javax.inject.Inject;

import models.Genre;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import repositories.GenreRepository;
import views.html.genre.index;
import views.html.genre.view;

import static java.util.Objects.requireNonNull;

public class GenreController extends Controller {

  private final GenreRepository genreRepository;
  private final FormFactory formFactory;

  @Inject
  public GenreController(GenreRepository genreRepository, FormFactory formFactory) {
    this.genreRepository = requireNonNull(genreRepository);
    this.formFactory = requireNonNull(formFactory);
  }

  public Result index() {
    return ok(index.render(genreRepository.findAll()));
  }

  public Result view(long id) {
    Optional<Genre> maybeGenre = genreRepository.findById(id);
    return ok(view.render(maybeGenre.get()));
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
