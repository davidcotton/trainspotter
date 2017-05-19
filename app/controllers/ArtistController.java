package controllers;

import static java.util.Objects.requireNonNull;
import static play.libs.Json.toJson;
import static play.mvc.Results.TODO;
import static play.mvc.Results.notFound;
import static play.mvc.Results.ok;

import javax.inject.Inject;
import play.mvc.Result;
import services.ArtistService;

public class ArtistController {

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
        .orElse(notFound(toJson("Not found")));
  }

  public Result create() {
    return TODO;
  }

  public Result update(long id) {
    return TODO;
  }

  public Result delete(long id) {
    return TODO;
  }
}
