package repositories;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;
import models.Genre;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.Test;

public class GenreRepositoryTest extends AbstractIntegrationTest {

  private final GenreRepository genreRepository;

  public GenreRepositoryTest() {
    genreRepository = new GenreRepository();
  }

  @Test
  public void findAll() throws Exception {
    List<Genre> genres = genreRepository.findAll();
    assertThat(genres, not(IsEmptyCollection.empty()));
    assertThat(genres.size(), is(2));
    assertThat(genres, hasItem(hasProperty("name", is("House"))));
    assertThat(genres, hasItem(hasProperty("name", is("Techno"))));
  }

  @Test public void findById_successGivenIdInDb() throws Exception {
    Optional<Genre> maybeGenre = genreRepository.findById(1L);
    assertTrue(maybeGenre.isPresent());
    assertEquals("House", maybeGenre.get().getName());
  }

  @Test public void findById_failureGivenIdNotInDb() throws Exception {
    Optional<Genre> maybeGenre = genreRepository.findById(999L);
    assertFalse(maybeGenre.isPresent());
  }

  @Test public void findByEmail_successGivenEmailIdInDb() throws Exception {
    Optional<Genre> maybeGenre = genreRepository.findByName("House");
    assertTrue(maybeGenre.isPresent());
    assertEquals("House", maybeGenre.get().getName());
  }

  @Test public void findByEmail_failureGivenEmailNotInDb() throws Exception {
    Optional<Genre> maybeGenre = genreRepository.findByName("Classical");
    assertFalse(maybeGenre.isPresent());
  }

  @Test public void insert_success() throws Exception {
    // ARRANGE
    Genre genre = new Genre(null, "Tech House", null, null, null, null);

    // ACT
    genreRepository.insert(genre);

    // ASSERT
    Optional<Genre> maybeGenre = genreRepository.findByName("Tech House");
    assertTrue(maybeGenre.isPresent());
    assertThat(maybeGenre.get().getName(), is("Tech House"));
    // verify that default fields are populated
    assertNotNull(maybeGenre.get().getId());
    assertNotNull(maybeGenre.get().getCreated());
    assertNotNull(maybeGenre.get().getUpdated());
  }

  @Test public void update_success() throws Exception {
    // ARRANGE
    // fetch an genre to update
    Genre genre = genreRepository.findById(2L).orElseThrow(Exception::new);
    // update data
    genre.setName("Deep House");

    // ACT
    genreRepository.update(genre);

    // ASSERT
    Optional<Genre> maybeGenre = genreRepository.findByName("Deep House");
    assertTrue(maybeGenre.isPresent());
    // verify that the genre saved correctly
    assertThat(maybeGenre.get().getId(), is(2L));
    assertThat(maybeGenre.get().getName(), is("Deep House"));
  }
}
