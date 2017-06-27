package services;

import com.avaje.ebean.PagedList;

import static java.util.Objects.requireNonNull;

import io.atlassian.fugue.Either;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import models.Artist;
import models.Artist.Status;
import models.create.CreateArtist;
import models.update.UpdateArtist;
import play.data.Form;
import repositories.ArtistRepository;

public class ArtistService {

  private final ArtistRepository artistRepository;

  @Inject
  public ArtistService(ArtistRepository artistRepository) {
    this.artistRepository = requireNonNull(artistRepository);
  }

  /**
   * Fetch all Artists.
   *
   * @return A collection of Artists.
   */
  public List<Artist> fetchAll() {
    return artistRepository.findAll();
  }

  public PagedList<Artist> fetchAllPaged(int page) {
    return artistRepository.findAllPaged(page);
  }

  /**
   * Find an Artist by their ID.
   *
   * @param id The ID to search for.
   * @return An optional Artist if found.
   */
  public Optional<Artist> findById(long id) {
    return artistRepository.findById(id);
  }

  /**
   * Find an Artist by their name.
   *
   * @param name The name to search for.
   * @return An optional Artist if found.
   */
  public Optional<Artist> findByName(String name) {
    return artistRepository.findByName(name);
  }

  public Optional<Artist> findBySlug(String slug) {
    return artistRepository.findBySlug(slug);
  }

  /**
   * Insert a new Artist.
   *
   * @param artistForm The submitted Artist data form.
   * @return Either the inserted Artist or the form with errors.
   */
  public Either<Form<CreateArtist>, Artist> insert(Form<CreateArtist> artistForm) {
    if (artistForm.hasErrors()) {
      return Either.left(artistForm);
    }

    Artist artist = new Artist(artistForm.get());
    // save to DB
    artistRepository.insert(artist);

    // return saved artist
    return Either.right(artist);
  }

  /**
   * Update an Artist.
   *
   * @param savedArtist The existing Artist.
   * @param artistForm  The new Artist data form.
   * @return Either the updated Artist or the form with errors.
   */
  public Either<Form<UpdateArtist>, Artist> update(Artist savedArtist, Form<UpdateArtist> artistForm) {
    if (artistForm.hasErrors()) {
      return Either.left(artistForm);
    }

    UpdateArtist updateArtist = artistForm.get();
    Artist newArtist = new Artist(updateArtist, savedArtist);

    // save to DB
    artistRepository.update(newArtist);

    // return saved artist
    return Either.right(newArtist);
  }

  /**
   * Delete an Artist.
   *
   * @param artist The Artist to delete.
   */
  public void delete(Artist artist) {
    artist.setStatus(Status.deleted);
    artistRepository.update(artist);
  }
}
