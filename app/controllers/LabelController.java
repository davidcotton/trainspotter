package controllers;

import static java.util.Objects.requireNonNull;
import static play.libs.Json.fromJson;
import static play.libs.Json.toJson;
import static utilities.JsonHelper.errorsAsJson;

import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import models.Label;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import repositories.LabelRepository;
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

  public Result index() {
    List<Label> labels = labelRepository.findAll();
    return ok(index.render(labels));
  }

  public Result view(long id) {
    Optional<Label> maybeLabel = labelRepository.findById(id);
    return ok(view.render(maybeLabel.get()));
  }

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
