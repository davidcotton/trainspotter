package controllers;

import static java.util.Objects.requireNonNull;

import static play.libs.Json.fromJson;
import static play.libs.Json.toJson;

import static utils.JsonHelper.MESSAGE_NOT_FOUND;
import static utils.JsonHelper.errorsAsJson;

import javax.inject.Inject;

import models.Tracklist;

import play.mvc.Controller;
import play.mvc.Result;

import services.TracklistService;

public class TracklistController extends Controller {

  private final TracklistService tracklistService;

  @Inject
  public TracklistController(TracklistService tracklistService) {
    this.tracklistService = requireNonNull(tracklistService);
  }

  public Result fetchAll() {
    return ok(toJson(tracklistService.fetchAll()));
  }

  public Result fetch(long id) {
    return tracklistService.findById(id)
        .map(tracklist -> ok(toJson(tracklist)))
        .orElse(notFound(errorsAsJson(MESSAGE_NOT_FOUND)));
  }

  public Result create() {
    return tracklistService
        .insert(fromJson(request().body().asJson(), Tracklist.class))
        .fold(
            error -> badRequest(errorsAsJson(error)),
            tracklist -> created(toJson(tracklist))
        );
  }

  public Result update(long id) {
    return tracklistService
        .findById(id)
        .map(savedTracklist -> tracklistService
            .update(savedTracklist, fromJson(request().body().asJson(), Tracklist.class))
            .fold(
                error -> badRequest(errorsAsJson(error)),
                newTracklist -> created(toJson(newTracklist))
            )
        )
        .orElse(notFound(errorsAsJson(MESSAGE_NOT_FOUND)));
  }

  public Result delete(long id) {
    return TODO;
  }
}
