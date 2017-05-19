package services;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import models.Track;
import play.data.FormFactory;
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


}
