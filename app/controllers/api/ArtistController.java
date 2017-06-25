package controllers.api;

import static java.util.Objects.requireNonNull;
import static play.libs.Json.toJson;
import static utilities.JsonHelper.MESSAGE_NOT_FOUND;
import static utilities.JsonHelper.errorsAsJson;

import javax.inject.Inject;
import models.CreateArtist;
import models.UpdateArtist;
import play.data.FormFactory;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import services.ArtistService;

public class ArtistController extends Controller {

  private final ArtistService artistService;
  private final FormFactory formFactory;

  @Inject
  public ArtistController(ArtistService artistService, FormFactory formFactory) {
    this.artistService = requireNonNull(artistService);
    this.formFactory = requireNonNull(formFactory);
  }

  public Result fetchAll() {
    return ok(toJson(artistService.fetchAll()));
  }

  public Result fetch(long id) {
    return artistService.findById(id)
        .map(artist -> ok(toJson(artist)))
        .orElse(notFound(errorsAsJson(MESSAGE_NOT_FOUND)));
  }

  @BodyParser.Of(BodyParser.Json.class)
  public Result create() {
    return artistService
        .insert(formFactory.form(CreateArtist.class).bindFromRequest())
        .fold(
            form -> badRequest(errorsAsJson(form.errors())),
            artist -> created(toJson(artist))
        );
  }

  @BodyParser.Of(BodyParser.Json.class)
  public Result update(long id) {
    return artistService
        .findById(id)
        .map(savedArtist -> artistService
            .update(savedArtist, formFactory.form(UpdateArtist.class).bindFromRequest())
            .fold(
                form -> badRequest(errorsAsJson(form.errors())),
                newArtist -> created(toJson(newArtist))
            )
        )
        .orElse(notFound(errorsAsJson(MESSAGE_NOT_FOUND)));
  }

  public Result delete(long id) {
    return TODO;
  }
}
