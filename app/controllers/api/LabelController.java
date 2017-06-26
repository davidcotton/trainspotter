package controllers.api;

import static java.util.Objects.requireNonNull;
import static play.libs.Json.toJson;
import static utilities.JsonHelper.MESSAGE_NOT_FOUND;
import static utilities.JsonHelper.errorsAsJson;

import javax.inject.Inject;
import models.create.CreateLabel;
import models.update.UpdateLabel;
import play.data.FormFactory;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import services.LabelService;

public class LabelController extends Controller {

  private final LabelService labelService;
  private final FormFactory formFactory;

  @Inject
  public LabelController(LabelService labelService, FormFactory formFactory) {
    this.labelService = requireNonNull(labelService);
    this.formFactory = requireNonNull(formFactory);
  }

  public Result fetchAll() {
    return ok(toJson(labelService.fetchAll()));
  }

  public Result fetch(long id) {
    return labelService.findById(id)
        .map(user -> ok(toJson(user)))
        .orElse(notFound(errorsAsJson(MESSAGE_NOT_FOUND)));
  }

  /**
   * Create a new Label.
   *
   * @return The new Label on success, else the errors.
   */
  @BodyParser.Of(BodyParser.Json.class)
  public Result create() {
    return labelService
        .insert(formFactory.form(CreateLabel.class).bindFromRequest())
        .fold(
            form -> badRequest(errorsAsJson(form.errors())),
            label -> created(toJson(label))
        );
  }

  /**
   * Update a Label.
   *
   * @param id The ID of the Label to update.
   * @return The updated Label on success, else the errors.
   */
  @BodyParser.Of(BodyParser.Json.class)
  public Result update(long id) {
    return labelService
        .findById(id)
        .map(savedLabel -> labelService
            .update(savedLabel, formFactory.form(UpdateLabel.class).bindFromRequest())
            .fold(
                form -> badRequest(errorsAsJson(form.errors())),
                newLabel -> created(toJson(newLabel))
            )
        )
        .orElse(notFound(errorsAsJson(MESSAGE_NOT_FOUND)));
  }

  /**
   * Delete a Label.
   *
   * @param id The ID of the Label to delete.
   * @return HTTP 204 No Content on success, else the errors.
   */
  public Result delete(long id) {
    return labelService
        .findById(id)
        .map(label -> {
          labelService.delete(label);
          return (Result) noContent();
        })
        .orElse(notFound(errorsAsJson(MESSAGE_NOT_FOUND)));
  }
}
