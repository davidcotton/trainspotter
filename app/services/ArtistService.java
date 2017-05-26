package services;

import static java.util.Objects.requireNonNull;

import static play.libs.Json.toJson;

import io.atlassian.fugue.Either;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

import models.Artist;

import play.data.Form;
import play.data.FormFactory;
import play.data.validation.ValidationError;

import repositories.ArtistRepository;

public class ArtistService {

  private final ArtistRepository artistRepository;
  private final FormFactory formFactory;

  @Inject
  public ArtistService(ArtistRepository artistRepository, FormFactory formFactory) {
    this.artistRepository = requireNonNull(artistRepository);
    this.formFactory = requireNonNull(formFactory);
  }

  /**
   * Fetch all artists.
   * @return A collection of all artists in the DB.
   */
  public List<Artist> fetchAll() {
    return artistRepository.findAll();
  }

  /**
   * Find an artist by their ID.
   * @param id  The ID to search for.
   * @return    An optional artist if found.
   */
  public Optional<Artist> findById(long id) {
    return artistRepository.findById(id);
  }

  /**
   * Insert a new artist.
   * @param artist  The artist to insert.
   * @return        Either the inserted artist or validation errors.
   */
  public Either<Map<String, List<ValidationError>>, Artist> insert(Artist artist) {
    // validate new artist
    Form<Artist> artistForm = formFactory
        .form(Artist.class, Artist.InsertValidators.class)
        .bind(toJson(artist));
    if (artistForm.hasErrors()) {
      // return validation errors
      return Either.left(artistForm.errors());
    }

    // save to DB
    artistRepository.insert(artist);

    // return saved artist
    return Either.right(artist);
  }

  /**
   * Update an artist.
   * @param savedArtist The existing artist data.
   * @param newArtist   The new artist data.
   * @return            Either the updated artist or validation errors.
   */
  public Either<Map<String, List<ValidationError>>, Artist> update(Artist savedArtist, Artist newArtist) {
    // copy over read only fields
    newArtist.setId(savedArtist.getId());
    newArtist.setCreated(savedArtist.getCreated());

    // validate the changes
    Form<Artist> artistForm = formFactory
        .form(Artist.class, Artist.UpdateValidators.class)
        .bind(toJson(newArtist));
    if (artistForm.hasErrors()) {
      // return validation errors
      return Either.left(artistForm.errors());
    }

    // save to DB
    artistRepository.update(newArtist);

    // return saved artist
    return Either.right(newArtist);
  }
}
