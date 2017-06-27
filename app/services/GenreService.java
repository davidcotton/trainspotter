package services;

import static java.util.Objects.requireNonNull;

import io.atlassian.fugue.Either;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import models.Genre;
import models.Genre.Status;
import models.create.CreateGenre;
import models.update.UpdateGenre;
import play.data.Form;
import repositories.GenreRepository;

public class GenreService {

  private final GenreRepository genreRepository;

  @Inject
  public GenreService(GenreRepository genreRepository) {
    this.genreRepository = requireNonNull(genreRepository);
  }

  /**
   * Fetch all Genres.
   *
   * @return A collection of genres.
   */
  public List<Genre> fetchAll() {
    return genreRepository.findAll();
  }

  /**
   * Find a Genre by its ID.
   *
   * @param id The ID to search for.
   * @return An optional Genre if found.
   */
  public Optional<Genre> findById(long id) {
    return genreRepository.findById(id);
  }

  /**
   * Find a Genre by its name.
   *
   * @param name The name of the Genre to search for.
   * @return An optional Genre if found.
   */
  public Optional<Genre> findByName(String name) {
    return genreRepository.findByName(name);
  }

  public Optional<Genre> findBySlug(String slug) {
    return genreRepository.findBySlug(slug);
  }

  /**
   * Insert a new Genre.
   *
   * @param genreForm The submitted Genre data form.
   * @return Either the inserted Genre or the form with errors.
   */
  public Either<Form<CreateGenre>, Genre> insert(Form<CreateGenre> genreForm) {
    if (genreForm.hasErrors()) {
      return Either.left(genreForm);
    }

    Genre genre = new Genre(genreForm.get());
    // save to DB
    genreRepository.insert(genre);

    // return saved genre
    return Either.right(genre);
  }

  /**
   * Update an Genre.
   *
   * @param savedGenre The existing Genre.
   * @param genreForm  The new Genre data form.
   * @return Either the updated Genre or the form with errors.
   */
  public Either<Form<UpdateGenre>, Genre> update(Genre savedGenre, Form<UpdateGenre> genreForm) {
    if (genreForm.hasErrors()) {
      return Either.left(genreForm);
    }

    UpdateGenre updateGenre = genreForm.get();
    Genre newGenre = new Genre(updateGenre, savedGenre);

    // save to DB
    genreRepository.update(newGenre);

    // return saved genre
    return Either.right(newGenre);
  }

  /**
   * Delete a Genre.
   *
   * @param genre The Genre to delete.
   */
  public void delete(Genre genre) {
    genre.setStatus(Status.deleted);
    genreRepository.update(genre);
  }
}
