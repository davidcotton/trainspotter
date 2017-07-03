package services;

import static java.util.Objects.requireNonNull;

import com.avaje.ebean.PagedList;
import io.atlassian.fugue.Either;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import models.Artist;
import models.Track;
import models.Track.Status;
import models.create.CreateTrack;
import models.update.UpdateTrack;
import play.data.Form;
import repositories.TrackRepository;

public class TrackService {

  private final TrackRepository trackRepository;

  @Inject
  public TrackService(TrackRepository trackRepository) {
    this.trackRepository = requireNonNull(trackRepository);
  }

  /**
   * Fetch all Tracks.
   * 
   * @return A collection of Tracks.
   */
  public List<Track> fetchAll() {
    return trackRepository.findAll();
  }

  public PagedList<Track> findAllPagedByArtist(Artist artist, int page) {
    return trackRepository.findAllPagedByArtist(artist, page);
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
   * @param trackForm The submitted Track data form.
   * @return Either the inserted Track or the form with errors.
   */
  public Either<Form<CreateTrack>, Track> insert(Form<CreateTrack> trackForm) {
    if (trackForm.hasErrors()) {
      return Either.left(trackForm);
    }

    Track track = new Track(trackForm.get());
    trackRepository.insert(track);

    return Either.right(track);
  }

  /**
   * Update an Track.
   *
   * @param savedTrack The existing Track.
   * @param trackForm  The new Track data form.
   * @return Either the updated Track or the form with errors.
   */
  public Either<Form<UpdateTrack>, Track> update(Track savedTrack, Form<UpdateTrack> trackForm) {
    if (trackForm.hasErrors()) {
      return Either.left(trackForm);
    }

    UpdateTrack updateTrack = trackForm.get();
    Track newTrack = new Track(updateTrack, savedTrack);
    trackRepository.update(newTrack);

    return Either.right(newTrack);
  }

  /**
   * Delete an Track.
   *
   * @param track The Track to delete.
   */
  public void delete(Track track) {
    track.setStatus(Status.deleted);
    trackRepository.update(track);
  }
}
