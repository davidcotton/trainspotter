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

  public List<Genre> findAll() {
    return genreRepository.findAll();
  }

  public Optional<Genre> findById(long id) {
    return genreRepository.findById(id);
  }

  public Either<Map<String, List<ValidationError>>, Genre> insert(Genre genre) {
    Form<Genre> genreForm = formFactory.form(Genre.class).bind(toJson(genre));
    if (genreForm.hasErrors()) {
      return Either.left(genreForm.errors());
    }

    genreRepository.insert(genre);

    return Either.right(genre);
  }

  public Either<Map<String, List<ValidationError>>, Genre> update(Genre savedGenre, Genre newGenre) {
    Form<Genre> genreForm = formFactory.form(Genre.class).bind(toJson(savedGenre));
    if (genreForm.hasErrors()) {
      return Either.left(genreForm.errors());
    }

    genreRepository.update(savedGenre);

    return Either.right(savedGenre);
  }

}
