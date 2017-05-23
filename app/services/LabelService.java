package services;

import static java.util.Objects.requireNonNull;
import static play.libs.Json.toJson;

import io.atlassian.fugue.Either;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.inject.Inject;
import models.Label;
import play.data.Form;
import play.data.FormFactory;
import play.data.validation.ValidationError;
import repositories.LabelRepository;

public class LabelService {

  private final LabelRepository labelRepository;
  private final FormFactory formFactory;

  @Inject
  public LabelService(LabelRepository labelRepository, FormFactory formFactory) {
    this.labelRepository = requireNonNull(labelRepository);
    this.formFactory = requireNonNull(formFactory);
  }

  public List<Label> findAll() {
    return labelRepository.findAll();
  }

  public Optional<Label> findById(long id) {
    return labelRepository.findById(id);
  }

  public Either<Map<String, List<ValidationError>>, Label> insert(Label label) {
    Form<Label> labelForm = formFactory.form(Label.class).bind(toJson(label));
    if (labelForm.hasErrors()) {
      return Either.left(labelForm.errors());
    }

    labelRepository.insert(label);

    return Either.right(label);
  }

  public Either<Map<String, List<ValidationError>>, Label> update(Label savedLabel, Label newLabel) {
    Form<Label> labelForm = formFactory.form(Label.class).bind(toJson(savedLabel));
    if (labelForm.hasErrors()) {
      return Either.left(labelForm.errors());
    }

    labelRepository.update(savedLabel);

    return Either.right(savedLabel);
  }
}
