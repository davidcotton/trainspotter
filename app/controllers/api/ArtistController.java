package controllers.api;

import static java.util.Objects.requireNonNull;
import static play.libs.Json.toJson;
import static utilities.JsonHelper.MESSAGE_NOT_FOUND;
import static utilities.JsonHelper.errorsAsJson;

import javax.inject.Inject;
import models.create.CreateArtist;
import models.update.UpdateArtist;
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

  /**
   * Fetch all Artists.
   *
   * @return A collection of Artists.
   */
  public Result fetchAll() {
    return ok(toJson(artistService.fetchAll()));
  }

  /**
   * Fetch a single Artist.
   *
   * @param id The ID of the Artist to fetch.
   * @return The Artist if found, else the error.
   */
  public Result fetch(long id) {
    return artistService.findById(id)
        .map(artist -> ok(toJson(artist)))
        .orElse(notFound(errorsAsJson(MESSAGE_NOT_FOUND)));
  }

  /**
   * Create a new Artist.
   *
   * @return The new Artist on success, else the errors.
   */
  @BodyParser.Of(BodyParser.Json.class)
  public Result create() {
    return artistService
        .insert(formFactory.form(CreateArtist.class).bindFromRequest())
        .fold(
            form -> badRequest(errorsAsJson(form.errors())),
            artist -> created(toJson(artist))
        );
  }

  /**
   * Update an Artist.
   *
   * @param id The ID of the Artist to update.
   * @return The updated Artist on success, else the errors.
   */
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

  /**
   * Delete an Artist.
   *
   * @param id The ID of the Artist to delete.
   * @return HTTP 204 No Content on success, else the errors.
   */
  public Result delete(long id) {
    return artistService
        .findById(id)
        .map(artist -> {
          artistService.delete(artist);
          return (Result) noContent();
        })
        .orElse(notFound(errorsAsJson(MESSAGE_NOT_FOUND)));
  }
}
