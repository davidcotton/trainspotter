package controllers.app;

import static java.util.Objects.requireNonNull;

import javax.inject.Inject;
import models.create.CreateLabel;
import models.update.UpdateLabel;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.Security;
import services.LabelService;
import views.html.label.add;
import views.html.label.edit;
import views.html.label.index;
import views.html.label.view;
import views.html.notFound;

public class LabelController extends Controller {

  private final LabelService labelService;
  private final FormFactory formFactory;

  @Inject
  public LabelController(LabelService labelService, FormFactory formFactory) {
    this.labelService = requireNonNull(labelService);
    this.formFactory = requireNonNull(formFactory);
  }

  /**
   * View all labels.
   *
   * @param page The page number to load.
   * @return A page with all labels.
   */
  public Result index(int page) {
    return ok(index.render(labelService.fetchAllPaged(page)));
  }

  /**
   * View a single label.
   *
   * @param slug The label's ID.
   * @return A label page if found.
   */
  public Result view(String slug) {
    return labelService
        .findBySlug(slug)
        .map(label -> ok(view.render(label)))
        .orElse(notFound(notFound.render()));
  }

  @play.mvc.Security.Authenticated(Secured.class)
  public Result addForm() {
    return ok(add.render(formFactory.form(CreateLabel.class)));
  }

  /**
   * Process the submission for creating a new Label.
   *
   * @return Redirect to the new Label on success else the form with errors.
   */
  @Security.Authenticated(Secured.class)
  public Result addSubmit() {
    return labelService
        .insert(formFactory.form(CreateLabel.class).bindFromRequest())
        .fold(
            form -> badRequest(add.render(form)),
            label -> Results.redirect(routes.LabelController.view(label.getSlug()))
        );
  }

  /**
   * Display an edit label page.
   *
   * @param slug The slug of the label.
   * @return An edit label page if found else not found page.
   */
  @Security.Authenticated(Secured.class)
  public Result editForm(String slug) {
    return labelService
        .findBySlug(slug)
        .map(label -> ok(edit.render(
            label,
            formFactory.form(UpdateLabel.class).fill(new UpdateLabel(label))
        )))
        .orElse(notFound(notFound.render()));
  }

  /**
   * Process the submission for updating a Label.
   *
   * @param slug The slug of the label.
   * @return Redirect to the updated Label on success else the form with errors.
   */
  @Security.Authenticated(Secured.class)
  public Result editSubmit(String slug) {
    return labelService
        .findBySlug(slug)
        .map(savedLabel -> labelService
            .update(savedLabel, formFactory.form(UpdateLabel.class).bindFromRequest())
            .fold(
                form -> badRequest(edit.render(savedLabel, form)),
                label -> Results.redirect(routes.LabelController.view(label.getSlug()))
            )
        )
        .orElse(notFound(notFound.render()));
  }

  /**
   * Delete a Label.
   *
   * @param slug The slug of the label.
   * @return Redirects to the Label index page on success else not found.
   */
  @Security.Authenticated(Secured.class)
  public Result delete(String slug) {
    return labelService
        .findBySlug(slug)
        .map(label -> {
          labelService.delete(label);
          return Results.redirect(routes.LabelController.index(1));
        })
        .orElse(notFound(notFound.render()));
  }
}
