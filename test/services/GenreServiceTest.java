package services;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.atlassian.fugue.Either;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import models.Genre;
import models.Genre.Status;
import models.create.CreateGenre;
import models.update.UpdateGenre;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import play.data.Form;
import repositories.GenreRepository;

@RunWith(MockitoJUnitRunner.class)
public class GenreServiceTest {
  
  @InjectMocks private GenreService genreService;
  @Mock private GenreRepository mockGenreRepository;
  @Mock private Form<CreateGenre> mockCreateGenreForm;
  @Mock private Form<UpdateGenre> mockUpdateGenreForm;

  @Test public void fetchAll() {
    when(mockGenreRepository.findAll()).thenReturn(new ArrayList<Genre>() {{
      add(mock(Genre.class));
      add(mock(Genre.class));
    }});

    List<Genre> actualGenres = genreService.fetchAll();

    assertThat(actualGenres, not(IsEmptyCollection.empty()));
    assertThat(actualGenres.size(), is(2));
  }

  @Test public void findById_givenIdInDb() {
    when(mockGenreRepository.findById(1L)).thenReturn(Optional.of(mock(Genre.class)));

    Optional<Genre> maybeGenre = genreService.findById(1L);

    assertTrue(maybeGenre.isPresent());
  }

  @Test public void findById_givenIdNotInDb() {
    long nonExistentId = 1L;
    when(mockGenreRepository.findById(nonExistentId)).thenReturn(Optional.empty());

    Optional<Genre> maybeGenre = genreService.findById(nonExistentId);

    assertFalse(maybeGenre.isPresent());
  }

  @Test public void findByName_givenNameInDb() {
    String genreName = "Techno";
    when(mockGenreRepository.findByName(genreName)).thenReturn(Optional.of(mock(Genre.class)));

    Optional<Genre> maybeGenre = genreService.findByName(genreName);

    assertTrue(maybeGenre.isPresent());
  }

  @Test public void findByName_givenNameNotInDb() {
    String genreName = "Avante Garde Jazz";
    when(mockGenreRepository.findByName(genreName)).thenReturn(Optional.empty());

    Optional<Genre> maybeGenre = genreService.findByName(genreName);

    assertFalse(maybeGenre.isPresent());
  }

  @Test public void findBySlug_givenSlugInDb() {
    String genreName = "techno";
    when(mockGenreRepository.findByName(genreName)).thenReturn(Optional.of(mock(Genre.class)));

    Optional<Genre> maybeGenre = genreService.findByName(genreName);

    assertTrue(maybeGenre.isPresent());
  }

  @Test public void findBySlug_givenSlugNameNotInDb() {
    String genreName = "avante-garde-jazz";
    when(mockGenreRepository.findByName(genreName)).thenReturn(Optional.empty());

    Optional<Genre> maybeGenre = genreService.findByName(genreName);

    assertFalse(maybeGenre.isPresent());
  }

  @Test public void insert_success() {
    // ARRANGE
    CreateGenre createGenre = new CreateGenre("House");

    when(mockCreateGenreForm.hasErrors()).thenReturn(false);
    when(mockCreateGenreForm.get()).thenReturn(createGenre);

    // ACT
    Either<Form<CreateGenre>, Genre> genreOrError = genreService.insert(mockCreateGenreForm);

    // ASSERT
    // assert left (error value) is not present
    assertFalse(genreOrError.isLeft());
    // assert right (success value) is present
    assertTrue(genreOrError.isRight());
    assertThat(genreOrError.right().get(), instanceOf(Genre.class));
    // verify that the genre repository inserted the new genre
    ArgumentCaptor<Genre> argument = ArgumentCaptor.forClass(Genre.class);
    verify(mockGenreRepository).insert(argument.capture());
    assertThat(argument.getValue().getName(), is("House"));
  }

  @Test public void insert_failureWhenFailsValidation() {
    // ARRANGE
    when(mockCreateGenreForm.hasErrors()).thenReturn(true);
    Genre mockGenre = mock(Genre.class);

    // ACT
    Either<Form<CreateGenre>, Genre> genreOrError = genreService.insert(mockCreateGenreForm);

    // ASSERT
    // assert right (success value) is not present
    assertFalse(genreOrError.isRight());
    // assert left (error value) is present
    assertTrue(genreOrError.isLeft());
    // verify that the genreRepository never tried to insert the invalid genre
    verify(mockGenreRepository, never()).insert(mockGenre);
  }

  @Test public void update_success() {
    // ARRANGE
    Genre savedGenre = new Genre(
        1L, "Tech House", "tech-house", null, null,
        Status.active, ZonedDateTime.now(), ZonedDateTime.now()
    );
    UpdateGenre updateGenre = new UpdateGenre("Deep House");

    when(mockUpdateGenreForm.hasErrors()).thenReturn(false);
    when(mockUpdateGenreForm.get()).thenReturn(updateGenre);

    // ACT
    Either<Form<UpdateGenre>, Genre> genreOrError = genreService.update(savedGenre, mockUpdateGenreForm);

    // ASSERT
    // assert left (error value) is not present
    assertFalse(genreOrError.isLeft());
    // assert right (success value) is present
    assertTrue(genreOrError.isRight());
    // verify that the user repository updated the user
    ArgumentCaptor<Genre> argument = ArgumentCaptor.forClass(Genre.class);
    verify(mockGenreRepository).update(argument.capture());
    assertThat(argument.getValue().getName(), is("Deep House"));
  }

  @Test public void update_failureWhenFailsValidation() {
    // ARRANGE
    Genre savedGenre = new Genre(
        1L, "Tech House", "tech-house", null, null,
        Status.active, ZonedDateTime.now(), ZonedDateTime.now()
    );
    Genre mockGenre = mock(Genre.class);

    when(mockUpdateGenreForm.hasErrors()).thenReturn(true);

    // ACT
    Either<Form<UpdateGenre>, Genre> genreOrError = genreService.update(savedGenre, mockUpdateGenreForm);

    // ASSERT
    // assert right (success value) is not present
    assertFalse(genreOrError.isRight());
    // assert left (error value) is present
    assertTrue(genreOrError.isLeft());
    // verify that the genreRepository never tried to insert the invalid genre
    verify(mockGenreRepository, never()).update(mockGenre);
  }
}
