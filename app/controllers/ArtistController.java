package controllers;

import static java.util.Objects.requireNonNull;
import static play.libs.Json.fromJson;
import static play.libs.Json.toJson;
import static utils.JsonHelper.MESSAGE_NOT_FOUND;
import static utils.JsonHelper.errorsAsJson;

import javax.inject.Inject;
import models.Artist;
import play.mvc.Controller;
import play.mvc.Result;
import services.ArtistService;

public class ArtistController extends Controller {

  private final ArtistService artistService;

  @Inject
  public ArtistController(ArtistService artistService) {
    this.artistService = requireNonNull(artistService);
  }

  public Result findAll() {
    return ok(toJson(artistService.findAll()));
  }

  public Result find(long id) {
    return artistService.findById(id)
        .map(artist -> ok(toJson(artist)))
        .orElse(notFound(toJson(MESSAGE_NOT_FOUND)));
  }

  public Result create() {
    return artistService
        .insert(fromJson(request().body().asJson(), Artist.class))
        .fold(
            error -> badRequest(errorsAsJson(error)),
            artist -> created(toJson(artist))
        );
  }

  public Result update(long id) {
    return TODO;
  }

  public Result delete(long id) {
    return TODO;
  }
}
