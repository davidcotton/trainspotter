package services;

import static java.util.Objects.requireNonNull;

import com.avaje.ebean.PagedList;
import io.atlassian.fugue.Either;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import models.Artist;
import models.Genre;
import models.Tracklist;
import models.Tracklist.Status;
import models.User;
import models.create.CreateTracklist;
import models.update.UpdateTracklist;
import play.data.Form;
import repositories.TracklistRepository;

public class TracklistService {

  private final TracklistRepository tracklistRepository;
  private final UserService userService;

  @Inject
  public TracklistService(TracklistRepository tracklistRepository, UserService userService) {
    this.tracklistRepository = requireNonNull(tracklistRepository);
    this.userService = requireNonNull(userService);
  }

  /**
   * Fetch all Tracklists.
   *
   * @return A collection of Tracklists.
   */
  public List<Tracklist> fetchAll() {
    return tracklistRepository.findAll();
  }

  public PagedList<Tracklist> fetchAllPaged(int page) {
    return tracklistRepository.findAllPaged(page);
  }

  public PagedList<Tracklist> findAllPagedByArtist(Artist artist, int page) {
    return tracklistRepository.findAllPagedByArtist(artist, page);
  }

  public PagedList<Tracklist> findAllPagedByGenre(Genre genre, int page) {
    return tracklistRepository.findAllPagedByGenre(genre, page);
  }

  public List<Tracklist> findMostPopular() {
    return tracklistRepository.findMostPopular();
  }

  /**
   * Find a Tracklist by its ID.
   *
   * @param id The ID to search for.
   * @return An optional Tracklist if found.
   */
  public Optional<Tracklist> findById(long id) {
    return tracklistRepository.findById(id);
  }

  /**
   * Insert a new Tracklist.
   *
   * @param tracklistForm The submitted Tracklist data form.
   * @return Either the inserted Tracklist or the form with errors.
   */
  public Either<Form<CreateTracklist>, Tracklist> insert(Form<CreateTracklist> tracklistForm, User user) {
    if (tracklistForm.hasErrors()) {
      return Either.left(tracklistForm);
    }

    // insert the new tracklist
    Tracklist tracklist = new Tracklist(tracklistForm.get());
    tracklist.setUser(user);
    tracklistRepository.insert(tracklist);

    // update the user
    userService.addTracklist(user);

    return Either.right(tracklist);
  }

  /**
   * Update an Tracklist.
   *
   * @param savedTracklist The existing Tracklist.
   * @param tracklistForm  The new Tracklist data form.
   * @return Either the updated Tracklist or the form with errors.
   */
  public Either<Form<UpdateTracklist>, Tracklist> update(Tracklist savedTracklist, Form<UpdateTracklist> tracklistForm) {
    if (tracklistForm.hasErrors()) {
      return Either.left(tracklistForm);
    }

    UpdateTracklist updateTracklist = tracklistForm.get();
    Tracklist newTracklist = new Tracklist(updateTracklist, savedTracklist);
    tracklistRepository.update(newTracklist);

    return Either.right(newTracklist);
  }

  /**
   * Delete an Tracklist.
   *
   * @param tracklist The Tracklist to delete.
   */
  public void delete(Tracklist tracklist) {
    tracklist.setStatus(Status.deleted);
    tracklistRepository.update(tracklist);
  }
}
