package services;

import static java.util.Objects.requireNonNull;

import static play.libs.Json.toJson;

import io.atlassian.fugue.Either;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

import models.Genre;

import play.data.Form;
import play.data.FormFactory;
import play.data.validation.ValidationError;

import repositories.GenreRepository;

public class GenreService {

  private final GenreRepository genreRepository;
  private final FormFactory formFactory;

  @Inject
  public GenreService(GenreRepository genreRepository, FormFactory formFactory) {
    this.genreRepository = requireNonNull(genreRepository);
    this.formFactory = requireNonNull(formFactory);
  }

  /**
   * Fetch all genres.
   * @return A collection of all genres in the DB.
   */
  public List<Genre> fetchAll() {
    return genreRepository.findAll();
  }

  /**
   * Find a genre by its ID.
   * @param id  The ID to search for.
   * @return    An optional genre if found.
   */
  public Optional<Genre> findById(long id) {
    return genreRepository.findById(id);
  }

  /**
   * Insert a new genre.
   * @param genre The genre to insert.
   * @return      Either the inserted genre or validation errors.
   */
  public Either<Map<String, List<ValidationError>>, Genre> insert(Genre genre) {
    // validate new genre
    Form<Genre> genreForm = formFactory
        .form(Genre.class, Genre.InsertValidators.class)
        .bind(toJson(genre));
    if (genreForm.hasErrors()) {
      // return validation errors
      return Either.left(genreForm.errors());
    }

    // save to DB
    genreRepository.insert(genre);

    // return saved genre
    return Either.right(genre);
  }

  /**
   * Update a genre.
   * @param savedGenre  The existing genre data.
   * @param newGenre    The new genre data.
   * @return            Either the updated genre or validation errors.
   */
  public Either<Map<String, List<ValidationError>>, Genre> update(Genre savedGenre, Genre newGenre) {
    // copy over read only fields
    newGenre.setId(savedGenre.getId());
    newGenre.setCreated(savedGenre.getCreated());

    // validate the changes
    Form<Genre> genreForm = formFactory
        .form(Genre.class, Genre.UpdateValidators.class)
        .bind(toJson(newGenre));
    if (genreForm.hasErrors()) {
      // return validation errors
      return Either.left(genreForm.errors());
    }

    // save to DB
    genreRepository.update(newGenre);

    // return saved genre
    return Either.right(newGenre);
  }

}
