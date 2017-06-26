package repositories;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.avaje.ebean.PagedList;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import models.Genre;
import models.Label;
import models.Track;
import models.Track.Status;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.Test;

public class TrackRepositoryTest extends AbstractIntegrationTest {

  private final TrackRepository trackRepository;
  private final GenreRepository genreRepository;
  private final LabelRepository labelRepository;

  public TrackRepositoryTest() {
    trackRepository = new TrackRepository();
    genreRepository = new GenreRepository();
    labelRepository = new LabelRepository();
  }

  @Test public void findAll() throws Exception {
    List<Track> tracks = trackRepository.findAll();
    assertThat(tracks, not(IsEmptyCollection.empty()));
    assertThat(tracks.size(), is(10));
    // verify ordered alphabetically by name
    assertThat(tracks.get(0).getName(), is("Airplane"));
    assertThat(tracks.get(1).getName(), is("Blackbox"));
    assertThat(tracks.get(2).getName(), is("Eros"));
  }

  @Test public void findById_successGivenIdInDb() throws Exception {
    Optional<Track> maybeTrack = trackRepository.findById(1L);
    assertTrue(maybeTrack.isPresent());
    assertEquals("Eros", maybeTrack.get().getName());
  }

  @Test public void findById_failureGivenIdNotInDb() throws Exception {
    Optional<Track> maybeTrack = trackRepository.findById(999L);
    assertFalse(maybeTrack.isPresent());
  }

  @Test public void findByName_successGivenNameInDb() throws Exception {
    Optional<Track> maybeTrack = trackRepository.findByName("The Field And The Sun");
    assertTrue(maybeTrack.isPresent());
    assertThat(maybeTrack.get().getId(), is(3L));
  }

  @Test public void findByName_failureGivenNameNotInDb() throws Exception {
    Optional<Track> maybeTrack = trackRepository.findByName("Ice Ice Baby");
    assertFalse(maybeTrack.isPresent());
  }

  @Test public void insert_success() throws Exception {
    // ARRANGE
    Genre house = genreRepository.findByName("House").orElseThrow(Exception::new);
    Label bedrock = labelRepository.findByName("Bedrock Records").orElseThrow(Exception::new);
    Track track = new Track(null, "Beautiful Strange", null, null, null, house, bedrock, LocalDate.now(), null, Status.active, null, null);

    // ACT
    trackRepository.insert(track);

    // ASSERT
    Optional<Track> maybeTrack = trackRepository.findByName("Beautiful Strange");
    assertTrue(maybeTrack.isPresent());
    // verify that default fields are populated
    assertNotNull(maybeTrack.get().getId());
    assertNotNull(maybeTrack.get().getCreated());
    assertNotNull(maybeTrack.get().getUpdated());
  }

  @Test public void update_success() throws Exception {
    // ARRANGE
    // fetch an track to update
    Track track = trackRepository.findById(1L).orElseThrow(Exception::new);
    // update data
    track.setName("Xpander");

    // ACT
    trackRepository.update(track);

    // ASSERT
    Optional<Track> maybeTrack = trackRepository.findById(1L);
    assertTrue(maybeTrack.isPresent());
    // verify that the track saved correctly
    assertThat(maybeTrack.get().getId(), is(1L));
    assertThat(maybeTrack.get().getName(), is("Xpander"));
  }

  @Test public void delete() throws Exception {
    Track track = trackRepository.findById(1L).orElseThrow(Exception::new);
    trackRepository.delete(track);
    Optional<Track> maybeTrack = trackRepository.findById(1L);
    assertFalse(maybeTrack.isPresent());
  }
}
