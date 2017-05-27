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

  /**
   * Fetch all Labels.
   *
   * @return A collection of all Labels in the DB.
   */
  public List<Label> fetchAll() {
    return labelRepository.findAll();
  }

  /**
   * Find a Label by its ID.
   *
   * @param id  The ID to search for.
   * @return    An optional Label if found.
   */
  public Optional<Label> findById(long id) {
    return labelRepository.findById(id);
  }

  /**
   * Find a Label by its name.
   *
   * @param name  The name of the Label to search for.
   * @return      An optional Label if found.
   */
  public Optional<Label> findByName(String name) {
    return labelRepository.findByName(name);
  }

  /**
   * Insert a new Label.
   *
   * @param label The Label to insert.
   * @return      Either the inserted Label or validation errors
   */
  public Either<Map<String, List<ValidationError>>, Label> insert(Label label) {
    // validate new label
    Form<Label> labelForm = formFactory
        .form(Label.class, Label.InsertValidators.class)
        .bind(toJson(label));
    if (labelForm.hasErrors()) {
      // return validation errors
      return Either.left(labelForm.errors());
    }

    // save to DB
    labelRepository.insert(label);

    // return the saved label
    return Either.right(label);
  }

  /**
   * Update a Label.
   *
   * @param savedLabel  The existing Label data.
   * @param newLabel    The new Label data.
   * @return            Either the updated data or validation errors.
   */
  public Either<Map<String, List<ValidationError>>, Label> update(Label savedLabel, Label newLabel) {
    // copy over read only fields
    newLabel.setId(savedLabel.getId());
    newLabel.setCreated(savedLabel.getCreated());

    // validate the changes
    Form<Label> labelForm = formFactory
        .form(Label.class, Label.UpdateValidators.class)
        .bind(toJson(newLabel));
    if (labelForm.hasErrors()) {
      // return validation errors
      return Either.left(labelForm.errors());
    }

    // save to DB
    labelRepository.update(newLabel);

    // return the saved label
    return Either.right(newLabel);
  }
}
