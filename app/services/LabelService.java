package services;

import static java.util.Objects.requireNonNull;

import com.avaje.ebean.PagedList;
import io.atlassian.fugue.Either;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import models.Label.Status;
import models.create.CreateLabel;
import models.Label;
import models.update.UpdateLabel;
import play.data.Form;
import repositories.LabelRepository;

public class LabelService {

  private final LabelRepository labelRepository;

  @Inject
  public LabelService(LabelRepository labelRepository) {
    this.labelRepository = requireNonNull(labelRepository);
  }

  /**
   * Fetch all Labels.
   *
   * @return A collection of Labels.
   */
  public List<Label> fetchAll() {
    return labelRepository.findAll();
  }

  public PagedList<Label> fetchAllPaged(int page) {
    return labelRepository.findAllPaged(page);
  }

  /**
   * Find a Label by its ID.
   *
   * @param id The ID to search for.
   * @return An optional Label if found.
   */
  public Optional<Label> findById(long id) {
    return labelRepository.findById(id);
  }

  /**
   * Find a Label by its name.
   *
   * @param name The name of the Label to search for.
   * @return An optional Label if found.
   */
  public Optional<Label> findByName(String name) {
    return labelRepository.findByName(name);
  }

  public Optional<Label> findBySlug(String slug) {
    return labelRepository.findBySlug(slug);
  }

  /**
   * Insert a new Label.
   *
   * @param labelForm The submitted Label data form.
   * @return Either the inserted Label or the form with errors.
   */
  public Either<Form<CreateLabel>, Label> insert(Form<CreateLabel> labelForm) {
    if (labelForm.hasErrors()) {
      return Either.left(labelForm);
    }

    Label label = new Label(labelForm.get());
    // save to DB
    labelRepository.insert(label);

    // return saved label
    return Either.right(label);
  }

  /**
   * Update an Label.
   *
   * @param savedLabel The existing Label.
   * @param labelForm  The new Label data form.
   * @return Either the updated Label or the form with errors.
   */
  public Either<Form<UpdateLabel>, Label> update(Label savedLabel, Form<UpdateLabel> labelForm) {
    if (labelForm.hasErrors()) {
      return Either.left(labelForm);
    }

    UpdateLabel updateLabel = labelForm.get();
    Label newLabel = new Label(updateLabel, savedLabel);

    // save to DB
    labelRepository.update(newLabel);

    // return saved label
    return Either.right(newLabel);
  }

  /**
   * Delete an Label.
   *
   * @param label The Label to delete.
   */
  public void delete(Label label) {
    label.setStatus(Status.deleted);
    labelRepository.update(label);
  }
}
