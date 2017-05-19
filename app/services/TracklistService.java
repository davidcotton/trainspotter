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

  public List<Tracklist> findAll() {
    return tracklistRepository.findAll();
  }

  public Optional<Tracklist> findById(long id) {
    return tracklistRepository.findById(id);
  }

  public Either<Map<String, List<ValidationError>>, Tracklist> insert(Tracklist tracklist) {
    Form<Tracklist> tracklistForm = formFactory.form(Tracklist.class).bind(toJson(tracklist));
    if (tracklistForm.hasErrors()) {
      return Either.left(tracklistForm.errors());
    }

    tracklistRepository.insert(tracklist);

    return Either.right(tracklist);
  }

  public Either<Map<String, List<ValidationError>>, Tracklist> update(Tracklist tracklist) {
    Form<Tracklist> tracklistForm = formFactory.form(Tracklist.class).bind(toJson(tracklist));
    if (tracklistForm.hasErrors()) {
      return Either.left(tracklistForm.errors());
    }

    tracklistRepository.update(tracklist);

    return Either.right(tracklist);
  }
}
