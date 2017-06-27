package controllers.api;

import static java.util.Objects.requireNonNull;
import static play.libs.Json.fromJson;
import static play.libs.Json.toJson;
import static utilities.JsonHelper.MESSAGE_NOT_FOUND;
import static utilities.JsonHelper.errorsAsJson;

import controllers.Security;
import javax.inject.Inject;
import models.create.CreateGenre;
import models.update.UpdateGenre;
import play.data.FormFactory;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import services.GenreService;

public class GenreController extends Controller {

  private final GenreService genreService;
  private final FormFactory formFactory;

  @Inject
  public GenreController(GenreService genreService, FormFactory formFactory) {
    this.genreService = requireNonNull(genreService);
    this.formFactory = requireNonNull(formFactory);
  }

  public Result fetchAll() {
    return ok(toJson(genreService.fetchAll()));
  }

  public Result fetch(long id) {
    return genreService.findById(id)
        .map(genre -> ok(toJson(genre)))
        .orElse(notFound(errorsAsJson(MESSAGE_NOT_FOUND)));
  }

  @Security.Authenticated
  @BodyParser.Of(BodyParser.Json.class)
  public Result create() {
    return genreService
        .insert(formFactory.form(CreateGenre.class).bindFromRequest())
        .fold(
            form -> badRequest(errorsAsJson(form.errors())),
            user -> created(toJson(user))
        );
  }

  @Security.Authenticated
  @BodyParser.Of(BodyParser.Json.class)
  public Result update(long id) {
    return genreService
        .findById(id)
        .map(savedGenre -> genreService
            .update(savedGenre, formFactory.form(UpdateGenre.class).bindFromRequest())
            .fold(
                form -> badRequest(errorsAsJson(form.errors())),
                newGenre -> created(toJson(newGenre))
            )
        )
        .orElse(notFound(errorsAsJson(MESSAGE_NOT_FOUND)));
  }

  @Security.Authenticated
  public Result delete(long id) {
    return genreService
        .findById(id)
        .map(genre -> {
          genreService.delete(genre);
          return (Result) noContent();
        })
        .orElse(notFound(errorsAsJson(MESSAGE_NOT_FOUND)));
  }
}
