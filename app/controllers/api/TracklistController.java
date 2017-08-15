package controllers.api;

import static java.util.Objects.requireNonNull;
import static play.libs.Json.toJson;
import static utilities.JsonHelper.MESSAGE_NOT_FOUND;
import static utilities.JsonHelper.errorsAsJson;

import controllers.Security;
import javax.inject.Inject;
import models.create.CreateTracklist;
import models.update.UpdateTracklist;
import play.data.FormFactory;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import services.TracklistService;
import services.UserService;

public class TracklistController extends Controller {

  private final TracklistService tracklistService;
  private final FormFactory formFactory;
  private final UserService userService;

  @Inject
  public TracklistController(
      TracklistService tracklistService,
      FormFactory formFactory,
      UserService userService
  ) {
    this.tracklistService = requireNonNull(tracklistService);
    this.formFactory = requireNonNull(formFactory);
    this.userService = requireNonNull(userService);
  }

  public Result fetchAll() {
    return ok(toJson(tracklistService.fetchAll()));
  }

  public Result fetch(long id) {
    return tracklistService.findById(id)
        .map(tracklist -> ok(toJson(tracklist)))
        .orElse(notFound(errorsAsJson(MESSAGE_NOT_FOUND)));
  }

  @Security.Authenticated
  @BodyParser.Of(BodyParser.Json.class)
  public Result create() {
    return userService
        .findActiveById(Long.parseLong(request().getHeader("X-Authorisation-User-Id")))
        .map(user -> tracklistService
            .insert(formFactory.form(CreateTracklist.class).bindFromRequest(), user)
            .fold(
                form -> badRequest(errorsAsJson(form.errors())),
                tracklist -> created(toJson(tracklist))
            ))
        .orElse(notFound(errorsAsJson(MESSAGE_NOT_FOUND)));
  }

  @Security.Authenticated
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

  @Security.Authenticated
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
