package repositories;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.avaje.ebean.PagedList;
import java.util.List;
import java.util.Optional;
import models.Artist;
import models.Artist.Status;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.Test;

public class ArtistRepositoryTest extends AbstractIntegrationTest {

  private final ArtistRepository artistRepository;

  public ArtistRepositoryTest() {
    artistRepository = new ArtistRepository();
  }

  @Test public void findAll() throws Exception {
    List<Artist> artists = artistRepository.findAll();
    assertThat(artists, not(IsEmptyCollection.empty()));
    assertThat(artists.size(), is(6));
    // verify ordered alphabetically by name
    assertThat(artists.get(0).getName(), is("Adam Beyer"));
    assertThat(artists.get(1).getName(), is("John Digweed"));
    assertThat(artists.get(2).getName(), is("Marc Romboy"));
  }

  @Test public void findAllPaged() throws Exception {
    PagedList<Artist> pagedArtists = artistRepository.findAllPaged(1);

    // verify the pagination object attributes
    assertThat(pagedArtists.getPageSize(), is(16));
    assertThat(pagedArtists.getTotalRowCount(), is(6));

    // verify the paginated list
    List<Artist> artists = pagedArtists.getList();
    assertThat(artists.get(0).getName(), is("Adam Beyer"));
    assertThat(artists.get(1).getName(), is("John Digweed"));
    assertThat(artists.get(2).getName(), is("Marc Romboy"));
    assertThat(artists.get(3).getName(), is("Sasha"));
    assertThat(artists.get(4).getName(), is("Solomun"));
    assertThat(artists.get(5).getName(), is("Umek"));
  }

  @Test public void findById_successGivenIdInDb() throws Exception {
    Optional<Artist> maybeArtist = artistRepository.findById(1L);
    assertTrue(maybeArtist.isPresent());
    assertEquals("John Digweed", maybeArtist.get().getName());
  }

  @Test public void findById_failureGivenIdNotInDb() throws Exception {
    Optional<Artist> maybeArtist = artistRepository.findById(999L);
    assertFalse(maybeArtist.isPresent());
  }

  @Test public void findByName_successGivenNameInDb() throws Exception {
    Optional<Artist> maybeArtist = artistRepository.findByName("John Digweed");
    assertTrue(maybeArtist.isPresent());
    assertEquals("John Digweed", maybeArtist.get().getName());
  }

  @Test public void findByName_failureGivenNameNotInDb() throws Exception {
    Optional<Artist> maybeArtist = artistRepository.findByName("Cher");
    assertFalse(maybeArtist.isPresent());
  }

  @Test public void findBySlug_successGivenSlugInDb() throws Exception {
    Optional<Artist> maybeArtist = artistRepository.findBySlug("john-digweed");
    assertTrue(maybeArtist.isPresent());
    assertEquals("John Digweed", maybeArtist.get().getName());
  }

  @Test public void findBySlug_failureGivenSlugNotInDb() throws Exception {
    Optional<Artist> maybeArtist = artistRepository.findBySlug("rick-astley");
    assertFalse(maybeArtist.isPresent());
  }

  @Test public void insert_success() throws Exception {
    // ARRANGE
    Artist artist = new Artist(null, "Sven Vath", "sven-vath", null, null, null, null, null, null, null, Status.active, null, null);

    // ACT
    artistRepository.insert(artist);

    // ASSERT
    Optional<Artist> maybeArtist = artistRepository.findByName("Sven Vath");
    assertTrue(maybeArtist.isPresent());
    assertThat(maybeArtist.get().getName(), is("Sven Vath"));
    // verify that default fields are populated
    assertNotNull(maybeArtist.get().getId());
    assertNotNull(maybeArtist.get().getCreated());
    assertNotNull(maybeArtist.get().getUpdated());
  }

  @Test public void update_success() throws Exception {
    // ARRANGE
    // fetch an artist to update
    Artist artist = artistRepository.findById(3L).orElseThrow(Exception::new);
    // update data
    artist.setName("Richie Hawtin");

    // ACT
    artistRepository.update(artist);

    // ASSERT
    Optional<Artist> maybeArtist = artistRepository.findByName("Richie Hawtin");
    assertTrue(maybeArtist.isPresent());
    // verify that the artist saved correctly
    assertThat(maybeArtist.get().getId(), is(3L));
    assertThat(maybeArtist.get().getName(), is("Richie Hawtin"));
    // verify that the original image field wasn't changed
    assertThat(maybeArtist.get().getImage(), is("adam-beyer.jpg"));
  }
}
