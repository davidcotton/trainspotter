package controllers;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import javax.inject.Inject;
import models.Label;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import repositories.LabelRepository;
import views.html.label.add;
import views.html.label.edit;
import views.html.label.index;
import views.html.label.view;

public class LabelController extends Controller {

  private final LabelRepository labelRepository;
  private final FormFactory formFactory;

  @Inject
  public LabelController(LabelRepository labelRepository, FormFactory formFactory) {
    this.labelRepository = requireNonNull(labelRepository);
    this.formFactory = requireNonNull(formFactory);
  }

  /**
   * View all labels.
   *
   * @return A page with all labels.
   */
  public Result index() {
    return ok(index.render(labelRepository.findAll()));
  }

  /**
   * View a single label.
   *
   * @param id The label's ID.
   * @return A label page if found.
   */
  public Result view(long id) {
    Optional<Label> maybeLabel = labelRepository.findById(id);
    return ok(view.render(maybeLabel.get()));
  }

  /**
   * Add a new label.
   *
   * @return
   */
  public Result add() {
    return TODO;
  }

  public Result edit(long id) {
    return TODO;
  }

  public Result delete(long id) {
    return TODO;
  }
}
