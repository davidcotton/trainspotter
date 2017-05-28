package controllers;

import static java.util.Objects.requireNonNull;

import static play.libs.Json.fromJson;
import static play.libs.Json.toJson;

import static utilities.JsonHelper.MESSAGE_NOT_FOUND;
import static utilities.JsonHelper.errorsAsJson;

import javax.inject.Inject;

import models.Label;

import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import services.LabelService;

public class LabelController extends Controller {

  private final LabelService labelService;

  @Inject
  public LabelController(LabelService labelService) {
    this.labelService = requireNonNull(labelService);
  }

  public Result fetchAll() {
    return ok(toJson(labelService.fetchAll()));
  }

  public Result fetch(long id) {
    return labelService.findById(id)
        .map(user -> ok(toJson(user)))
        .orElse(notFound(errorsAsJson(MESSAGE_NOT_FOUND)));
  }

  @BodyParser.Of(BodyParser.Json.class)
  public Result create() {
    return labelService
        .insert(fromJson(request().body().asJson(), Label.class))
        .fold(
            error -> badRequest(errorsAsJson(error)),
            label -> created(toJson(label))
        );
  }

  @BodyParser.Of(BodyParser.Json.class)
  public Result update(long id) {
    return labelService
        .findById(id)
        .map(savedLabel -> labelService
            .update(savedLabel, fromJson(request().body().asJson(), Label.class))
            .fold(
                error -> badRequest(errorsAsJson(error)),
                newLabel -> created(toJson(newLabel))
            )
        )
        .orElse(notFound(errorsAsJson(MESSAGE_NOT_FOUND)));
  }

  public Result delete(long id) {
    return TODO;
  }
}
