package repositories;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.avaje.ebean.PagedList;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import models.Tracklist;
import models.User;
import org.junit.Test;

public class TracklistRepositoryTest extends AbstractIntegrationTest {

  private final TracklistRepository tracklistRepository;
  private final UserRepository userRepository;

  public TracklistRepositoryTest() {
    tracklistRepository = new TracklistRepository();
    userRepository = new UserRepository();
  }

  @Test public void findAll() throws Exception {
    List<Tracklist> tracklists = tracklistRepository.findAll();
    assertThat(tracklists.size(), is(4));
    // verify ordered alphabetically by name
    assertThat(tracklists.get(0).getName(), is("Sasha @ Last Night On Earth 025 (Coachella Festival, United States)"));
    assertThat(tracklists.get(1).getName(), is("Adam Beyer @ Drumcode 354 (Metro City Perth, Australia 2017-04-24)"));
    assertThat(tracklists.get(2).getName(), is("Transitions 657"));
    assertThat(tracklists.get(3).getName(), is("Sasha & John Digweed @ Carl Cox & Friends, Megastructure Stage, Ultra Music Festival Miami, MMW, United States"));
  }

  @Test public void findAllPaged() throws Exception {
    PagedList<Tracklist> pagedTracklists = tracklistRepository.findAllPaged(1);

    // verify the pagination object attributes
    assertThat(pagedTracklists.getPageSize(), is(10));
    assertThat(pagedTracklists.getTotalRowCount(), is(4));

    // verify the paginated list
    List<Tracklist> tracklists = pagedTracklists.getList();
    assertThat(tracklists.get(0).getName(), is("Sasha @ Last Night On Earth 025 (Coachella Festival, United States)"));
    assertThat(tracklists.get(1).getName(), is("Adam Beyer @ Drumcode 354 (Metro City Perth, Australia 2017-04-24)"));
    assertThat(tracklists.get(2).getName(), is("Transitions 657"));
    assertThat(tracklists.get(3).getName(), is("Sasha & John Digweed @ Carl Cox & Friends, Megastructure Stage, Ultra Music Festival Miami, MMW, United States"));
  }

  @Test public void findById_successGivenIdInDb() throws Exception {
    Optional<Tracklist> maybeTracklist = tracklistRepository.findById(2L);
    assertTrue(maybeTracklist.isPresent());
    assertEquals("Transitions 657", maybeTracklist.get().getName());
  }

  @Test public void findById_failureGivenIdNotInDb() throws Exception {
    Optional<Tracklist> maybeTracklist = tracklistRepository.findById(999L);
    assertFalse(maybeTracklist.isPresent());
  }

  @Test public void findBySlug_successGivenSlugInDb() throws Exception {
    Optional<Tracklist> maybeTracklist = tracklistRepository.findBySlug("transitions-657");
    assertTrue(maybeTracklist.isPresent());
    assertEquals("Transitions 657", maybeTracklist.get().getName());
  }

  @Test public void findBySlug_failureGivenSlugNotInDb() throws Exception {
    Optional<Tracklist> maybeTracklist = tracklistRepository.findBySlug("not-a-real-tracklist");
    assertFalse(maybeTracklist.isPresent());
  }

  @Test public void insert_success() throws Exception {
    // ARRANGE
    User user = userRepository.findById(1L).orElseThrow(Exception::new);
    Tracklist tracklist = new Tracklist(null, "Transitions 658", "transitions-658", LocalDate.now(), null, user, null, null, null, null, null, null);

    // ACT
    tracklistRepository.insert(tracklist);

    // ASSERT
    Optional<Tracklist> maybeTracklist = tracklistRepository.findBySlug("transitions-658");
    assertTrue(maybeTracklist.isPresent());
    assertThat(maybeTracklist.get().getName(), is("Transitions 658"));
    // verify that default fields are populated
    assertNotNull(maybeTracklist.get().getId());
    assertNotNull(maybeTracklist.get().getCreated());
    assertNotNull(maybeTracklist.get().getUpdated());
  }

  @Test public void update_success() throws Exception {
    // ARRANGE
    // fetch an tracklist to update
    Tracklist tracklist = tracklistRepository.findById(1L).orElseThrow(Exception::new);
    // update data
    tracklist.setName("Transitions 659");

    // ACT
    tracklistRepository.update(tracklist);

    // ASSERT
    Optional<Tracklist> maybeTracklist = tracklistRepository.findById(1L);
    assertTrue(maybeTracklist.isPresent());
    // verify that the tracklist saved correctly
    assertThat(maybeTracklist.get().getName(), is("Transitions 659"));
    // verify that the original image field wasn't changed
    assertThat(maybeTracklist.get().getSlug(), is("umf"));
  }

  // @todo will need to fix how the DB cascades
//  @Test public void delete() throws Exception {
//    Tracklist tracklist = tracklistRepository.findById(1L).orElseThrow(Exception::new);
//    tracklistRepository.delete(tracklist);
//    Optional<Tracklist> maybeTracklist = tracklistRepository.findById(1L);
//    assertFalse(maybeTracklist.isPresent());
//  }
}
