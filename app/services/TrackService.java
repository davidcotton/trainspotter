package services;

import static java.util.Objects.requireNonNull;
import static play.libs.Json.toJson;

import io.atlassian.fugue.Either;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.inject.Inject;
import models.Track;
import play.data.Form;
import play.data.FormFactory;
import play.data.validation.ValidationError;
import repositories.TrackRepository;

public class TrackService {

  private final TrackRepository trackRepository;
  private final FormFactory formFactory;

  @Inject
  public TrackService(TrackRepository trackRepository, FormFactory formFactory) {
    this.trackRepository = requireNonNull(trackRepository);
    this.formFactory = requireNonNull(formFactory);
  }

  /**
   * Fetch all Tracks.
   * 
   * @return A collection of Tracks.
   */
  public List<Track> fetchAll() {
    return trackRepository.findAll();
  }

  /**
   * Find a Track by its ID.
   * 
   * @param id The ID to search for.
   * @return An optional Track if found.
   */
  public Optional<Track> findById(long id) {
    return trackRepository.findById(id);
  }

  /**
   * Find a Track by its name.
   *
   * @param name The name of the Track to search for.
   * @return An optional Track if found.
   */
  public Optional<Track> findByName(String name) {
    return trackRepository.findByName(name);
  }

  /**
   * Insert a new Track.
   * 
   * @param track The Track data to insert.
   * @return Either the inserted Track or validation errors.
   */
  public Either<Map<String, List<ValidationError>>, Track> insert(Track track) {
    // validate new track
    Form<Track> trackForm = formFactory
        .form(Track.class, Track.InsertValidators.class)
        .bind(toJson(track));
    if (trackForm.hasErrors()) {
      // return validation errors
      return Either.left(trackForm.errors());
    }

    // save to DB
    trackRepository.insert(track);

    // return saved track
    return Either.right(track);
  }

  /**
   * Update a Track.
   * 
   * @param savedTrack  The existing Track data.
   * @param newTrack    The new Track data.
   * @return Either the updated Track or validation errors.
   */
  public Either<Map<String, List<ValidationError>>, Track> update(Track savedTrack, Track newTrack) {
    // copy over read only fields
    newTrack.setId(savedTrack.getId());
    newTrack.setCreated(savedTrack.getCreated());

    // validate the changes
    Form<Track> trackForm = formFactory
        .form(Track.class, Track.UpdateValidators.class)
        .bind(toJson(newTrack));
    if (trackForm.hasErrors()) {
      // return validation errors
      return Either.left(trackForm.errors());
    }

    // save to DB
    trackRepository.update(newTrack);

    // return saved track
    return Either.right(newTrack);
  }

}
