package controllers.api;

import static java.util.Objects.requireNonNull;
import static play.libs.Json.toJson;
import static utilities.JsonHelper.MESSAGE_NOT_FOUND;
import static utilities.JsonHelper.errorsAsJson;

import javax.inject.Inject;
import models.create.CreateTrack;
import models.update.UpdateTrack;
import play.data.FormFactory;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import services.TrackService;

public class TrackController extends Controller {

  private final TrackService trackService;
  private final FormFactory formFactory;

  @Inject
  public TrackController(TrackService trackService, FormFactory formFactory) {
    this.trackService = requireNonNull(trackService);
    this.formFactory = requireNonNull(formFactory);
  }

  public Result fetchAll() {
    return ok(toJson(trackService.fetchAll()));
  }

  public Result fetch(long id) {
    return trackService.findById(id)
        .map(track -> ok(toJson(track)))
        .orElse(notFound(errorsAsJson(MESSAGE_NOT_FOUND)));
  }

  @BodyParser.Of(BodyParser.Json.class)
  public Result create() {
    return trackService
        .insert(formFactory.form(CreateTrack.class).bindFromRequest())
        .fold(
            form -> badRequest(errorsAsJson(form.errors())),
            track -> created(toJson(track))
        );
  }

  @BodyParser.Of(BodyParser.Json.class)
  public Result update(long id) {
    return trackService
        .findById(id)
        .map(savedTrack -> trackService
            .update(savedTrack, formFactory.form(UpdateTrack.class).bindFromRequest())
            .fold(
                form -> badRequest(errorsAsJson(form.errors())),
                newTrack -> created(toJson(newTrack))
            )
        )
        .orElse(notFound(errorsAsJson(MESSAGE_NOT_FOUND)));
  }

  public Result delete(long id) {
    return trackService
        .findById(id)
        .map(track -> {
          trackService.delete(track);
          return (Result) noContent();
        })
        .orElse(notFound(errorsAsJson(MESSAGE_NOT_FOUND)));
  }
}
