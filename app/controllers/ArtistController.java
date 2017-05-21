package controllers;

import static java.util.Objects.requireNonNull;
import static play.libs.Json.fromJson;
import static play.libs.Json.toJson;
import static utils.JsonHelper.errorsAsJson;

import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import models.Artist;
import play.mvc.Controller;
import play.mvc.Result;
import services.ArtistService;
import views.html.artist.index;
import views.html.artist.view;

public class ArtistController extends Controller {

  private final ArtistService artistService;

  @Inject
  public ArtistController(ArtistService artistService) {
    this.artistService = requireNonNull(artistService);
  }

  public Result findAll() {
    List<Artist> artists = artistService.findAll();
    return ok(index.render(artists));
  }

  public Result find(long id) {
     Optional<Artist> maybeArtist = artistService.findById(id);
     return ok(view.render(maybeArtist.get()));
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
