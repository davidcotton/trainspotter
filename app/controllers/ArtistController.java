package controllers;

import static java.util.Objects.requireNonNull;

import static play.libs.Json.fromJson;
import static play.libs.Json.toJson;

import static utilities.JsonHelper.MESSAGE_NOT_FOUND;
import static utilities.JsonHelper.errorsAsJson;

import javax.inject.Inject;

import models.Artist;

import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import services.ArtistService;

public class ArtistController extends Controller {

  private final ArtistService artistService;

  @Inject
  public ArtistController(ArtistService artistService) {
    this.artistService = requireNonNull(artistService);
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
        .insert(fromJson(request().body().asJson(), Artist.class))
        .fold(
            error -> badRequest(errorsAsJson(error)),
            artist -> created(toJson(artist))
        );
  }

  @BodyParser.Of(BodyParser.Json.class)
  public Result update(long id) {
    return artistService
        .findById(id)
        .map(savedArtist -> artistService
            .update(savedArtist, fromJson(request().body().asJson(), Artist.class))
            .fold(
                error -> badRequest(errorsAsJson(error)),
                newArtist -> created(toJson(newArtist))
            )
        )
        .orElse(notFound(errorsAsJson(MESSAGE_NOT_FOUND)));
  }

  public Result delete(long id) {
    return TODO;
  }
}
