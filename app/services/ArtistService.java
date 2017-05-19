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

  public List<Artist> findAll() {
    return artistRepository.findAll();
  }

  public Optional<Artist> findById(long id) {
    return artistRepository.findById(id);
  }

  public Either<Map<String, List<ValidationError>>, Artist> insert(Artist artist) {
    Form<Artist> artistForm = formFactory.form(Artist.class).bind(toJson(artist));
    if (artistForm.hasErrors()) {
      return Either.left(artistForm.errors());
    }

    artistRepository.insert(artist);

    return Either.right(artist);
  }

  public Either<Map<String, List<ValidationError>>, Artist> update(Artist artist) {
    Form<Artist> artistForm = formFactory.form(Artist.class).bind(toJson(artist));
    if (artistForm.hasErrors()) {
      return Either.left(artistForm.errors());
    }

    artistRepository.update(artist);

    return Either.right(artist);
  }
}
