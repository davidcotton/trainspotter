package controllers;

import static java.util.Objects.requireNonNull;

import static play.libs.Json.fromJson;
import static play.libs.Json.toJson;

import static utilities.JsonHelper.MESSAGE_NOT_FOUND;
import static utilities.JsonHelper.errorsAsJson;

import javax.inject.Inject;

import models.Genre;

import play.mvc.Controller;
import play.mvc.Result;

import services.GenreService;

public class GenreController extends Controller {

  private final GenreService genreService;

  @Inject
  public GenreController(GenreService genreService) {
    this.genreService = requireNonNull(genreService);
  }

  public Result fetchAll() {
    return ok(toJson(genreService.fetchAll()));
  }

  public Result fetch(long id) {
    return genreService.findById(id)
        .map(genre -> ok(toJson(genre)))
        .orElse(notFound(errorsAsJson(MESSAGE_NOT_FOUND)));
  }

  public Result create() {
    return genreService
        .insert(fromJson(request().body().asJson(), Genre.class))
        .fold(
            error -> badRequest(errorsAsJson(error)),
            user -> created(toJson(user))
        );
  }

  public Result update(long id) {
    return genreService
        .findById(id)
        .map(savedGenre -> genreService
            .update(savedGenre, fromJson(request().body().asJson(), Genre.class))
            .fold(
                error -> badRequest(errorsAsJson(error)),
                newGenre -> created(toJson(newGenre))
            )
        )
        .orElse(notFound(errorsAsJson(MESSAGE_NOT_FOUND)));
  }

  public Result delete(long id) {
    return TODO;
  }
}
