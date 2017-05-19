package services;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import models.Artist;
import play.data.FormFactory;
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


}
