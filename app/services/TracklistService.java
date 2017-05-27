package services;

import static java.util.Objects.requireNonNull;

import static play.libs.Json.toJson;

import io.atlassian.fugue.Either;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

import models.Tracklist;

import play.data.Form;
import play.data.FormFactory;
import play.data.validation.ValidationError;

import repositories.TracklistRepository;

public class TracklistService {

  private final TracklistRepository tracklistRepository;
  private final FormFactory formFactory;

  @Inject
  public TracklistService(TracklistRepository tracklistRepository, FormFactory formFactory) {
    this.tracklistRepository = requireNonNull(tracklistRepository);
    this.formFactory = requireNonNull(formFactory);
  }

  /**
   * Fetch all Tracklists.
   *
   * @return A collection of all Tracklists in the DB.
   */
  public List<Tracklist> fetchAll() {
    return tracklistRepository.findAll();
  }

  /**
   * Find a Tracklist by its ID.
   *
   * @param id  The ID to search for.
   * @return    An optional Tracklist if found.
   */
  public Optional<Tracklist> findById(long id) {
    return tracklistRepository.findById(id);
  }

  /**
   * Insert a new Tracklist.
   *
   * @param tracklist The Tracklist to insert.
   * @return          Either the inserted Tracklist or validation errors.
   */
  public Either<Map<String, List<ValidationError>>, Tracklist> insert(Tracklist tracklist) {
    // validate new tracklist
    Form<Tracklist> tracklistForm = formFactory
        .form(Tracklist.class, Tracklist.InsertValidators.class)
        .bind(toJson(tracklist));
    if (tracklistForm.hasErrors()) {
      // return validation errors
      return Either.left(tracklistForm.errors());
    }

    // save to DB
    tracklistRepository.insert(tracklist);

    // return saved tracklist
    return Either.right(tracklist);
  }

  /**
   * Update a Tracklist.
   *
   * @param savedTracklist  The existing Tracklist data.
   * @param newTracklist    The new Tracklist data.
   * @return                Either the updated Tracklist or validation errors.
   */
  public Either<Map<String, List<ValidationError>>, Tracklist> update(Tracklist savedTracklist, Tracklist newTracklist) {
    // copy over read only fields
    newTracklist.setId(savedTracklist.getId());
    newTracklist.setCreated(savedTracklist.getCreated());

    // validate the changes
    Form<Tracklist> tracklistForm = formFactory
        .form(Tracklist.class, Tracklist.UpdateValidators.class)
        .bind(toJson(newTracklist));
    if (tracklistForm.hasErrors()) {
      // return validation errors
      return Either.left(tracklistForm.errors());
    }

    // save to DB
    tracklistRepository.update(newTracklist);

    // return saved tracklist
    return Either.right(newTracklist);
  }
}
