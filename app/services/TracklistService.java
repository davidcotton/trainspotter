package services;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import models.Tracklist;
import play.data.FormFactory;
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
}
