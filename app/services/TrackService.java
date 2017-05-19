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

  public List<Track> findAll() {
    return trackRepository.findAll();
  }

  public Optional<Track> findById(long id) {
    return trackRepository.findById(id);
  }

  public Either<Map<String, List<ValidationError>>, Track> insert(Track track) {
    Form<Track> trackForm = formFactory.form(Track.class).bind(toJson(track));
    if (trackForm.hasErrors()) {
      return Either.left(trackForm.errors());
    }

    trackRepository.insert(track);

    return Either.right(track);
  }

  public Either<Map<String, List<ValidationError>>, Track> update(Track track) {
    Form<Track> trackForm = formFactory.form(Track.class).bind(toJson(track));
    if (trackForm.hasErrors()) {
      return Either.left(trackForm.errors());
    }

    trackRepository.update(track);

    return Either.right(track);
  }
}
