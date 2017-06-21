package controllers;

import static java.util.Objects.requireNonNull;

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
import views.html.notFound;

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
   * @param slug The label's ID.
   * @return A label page if found.
   */
  public Result view(String slug) {
    return labelRepository
        .findBySlug(slug)
        .map(label -> ok(view.render(label)))
        .orElse(notFound(notFound.render()));
  }

  public Result addForm() {
    return ok(add.render(formFactory.form(Label.class)));
  }

  public Result addSubmit() {
    return TODO;
  }

  public Result editForm(String slug) {
    return labelRepository
        .findBySlug(slug)
        .map(label -> ok(edit.render(label, formFactory.form(Label.class).fill(label))))
        .orElse(notFound(notFound.render()));
  }

  public Result editSubmit(String slug) {
    return TODO;
  }

  public Result delete(String slug) {
    return TODO;
  }
}
