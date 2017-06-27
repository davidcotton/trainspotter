package controllers.api;

import static java.util.Objects.requireNonNull;
import static play.libs.Json.toJson;
import static utilities.JsonHelper.MESSAGE_NOT_FOUND;
import static utilities.JsonHelper.errorsAsJson;

import javax.inject.Inject;
import models.create.CreateTracklist;
import models.update.UpdateTracklist;
import play.data.FormFactory;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import services.TracklistService;

public class TracklistController extends Controller {

  private final TracklistService tracklistService;
  private final FormFactory formFactory;

  @Inject
  public TracklistController(TracklistService tracklistService, FormFactory formFactory) {
    this.tracklistService = requireNonNull(tracklistService);
    this.formFactory = requireNonNull(formFactory);
  }

  public Result fetchAll() {
    return ok(toJson(tracklistService.fetchAll()));
  }

  public Result fetch(long id) {
    return tracklistService.findById(id)
        .map(tracklist -> ok(toJson(tracklist)))
        .orElse(notFound(errorsAsJson(MESSAGE_NOT_FOUND)));
  }

  @BodyParser.Of(BodyParser.Json.class)
  public Result create() {
    return tracklistService
        .insert(formFactory.form(CreateTracklist.class).bindFromRequest())
        .fold(
            form -> badRequest(errorsAsJson(form.errors())),
            tracklist -> created(toJson(tracklist))
        );
  }

  @BodyParser.Of(BodyParser.Json.class)
  public Result update(long id) {
    return tracklistService
        .findById(id)
        .map(savedTracklist -> tracklistService
            .update(savedTracklist, formFactory.form(UpdateTracklist.class).bindFromRequest())
            .fold(
                form -> badRequest(errorsAsJson(form.errors())),
                newTracklist -> created(toJson(newTracklist))
            )
        )
        .orElse(notFound(errorsAsJson(MESSAGE_NOT_FOUND)));
  }

  public Result delete(long id) {
    return tracklistService
        .findById(id)
        .map(tracklist -> {
          tracklistService.delete(tracklist);
          return (Result) noContent();
        })
        .orElse(notFound(errorsAsJson(MESSAGE_NOT_FOUND)));
  }
}
