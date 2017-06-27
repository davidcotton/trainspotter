package services;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.atlassian.fugue.Either;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import models.Genre;
import models.create.CreateGenre;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import play.data.Form;
import play.data.FormFactory;
import repositories.GenreRepository;

@RunWith(MockitoJUnitRunner.class)
public class GenreServiceTest {
  
  @InjectMocks private GenreService genreService;
  @Mock private GenreRepository mockGenreRepository;
  @Mock private FormFactory mockFormFactory;
  @Mock private Form mockForm;
  @Mock private Form mockDataForm;

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

    assertThat(maybeGenre.isPresent(), is(false));
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

    assertThat(maybeGenre.isPresent(), is(false));
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

    assertThat(maybeGenre.isPresent(), is(false));
  }

  @Test public void insert_successGivenValidData() {
    // ARRANGE
    Form<CreateGenre> genreForm = new Form<>(CreateGenre.class, null, null, null);

    when(genreForm.hasErrors()).thenReturn(false);

    // ACT
    Either<Form<CreateGenre>, Genre> genreOrError = genreService.insert(genreForm);

    // ASSERT
    // assert left (error value) is not present
    assertFalse(genreOrError.isLeft());
    // assert right (success value) is present
    assertTrue(genreOrError.isRight());
    assertThat(genreOrError.right().get(), instanceOf(Genre.class));
    // verify that the user repository inserted the new user
    ArgumentCaptor<Genre> argument = ArgumentCaptor.forClass(Genre.class);
    verify(mockGenreRepository).insert(argument.capture());
    assertThat(argument.getValue().getName(), is("Techno"));
    assertThat(argument.getValue().getSlug(), is("techno"));
  }
//
//  @Test
//  public void insert_failureGivenInvalidData() {
//    // ARRANGE
//    Genre genre = new Genre(null, null, null, null, null, null);
//
//    Map<String, List<ValidationError>> validationErrors =
//        new HashMap<String, List<ValidationError>>() {{
//          put("name", mock(List.class));
//        }};
//
//    when(mockFormFactory.form(Genre.class, Genre.InsertValidators.class)).thenReturn(mockDataForm);
//    when(mockDataForm.bind(any(JsonNode.class))).thenReturn(mockForm);
//    when(mockForm.hasErrors()).thenReturn(true);
//    when(mockForm.errors()).thenReturn(validationErrors);
//
//    // ACT
//    Either<Map<String, List<ValidationError>>, Genre> genreOrError = genreService.insert(genre);
//
//    // ASSERT
//    // assert right (success value) is not present
//    assertFalse(genreOrError.isRight());
//    // assert left (error value) is present
//    assertTrue(genreOrError.isLeft());
//    assertThat(genreOrError.left().get().get("name"), instanceOf(List.class));
//    // verify that the genreRepository never tried to insert the invalid genre
//    verify(mockGenreRepository, never()).insert(any());
//  }
//
//  @Test
//  public void update_successGivenValidData() {
//    // ARRANGE
//    Genre savedGenre = new Genre(
//        1L, "Techno", new ArrayList<>(), new ArrayList<>(), ZonedDateTime.now(),
//        ZonedDateTime.now()
//    );
//    Genre newGenre = new Genre(
//        1L, "Deep House", new ArrayList<>(), new ArrayList<>(), ZonedDateTime.now(),
//        ZonedDateTime.now()
//    );
//
//    when(mockFormFactory.form(Genre.class, Genre.UpdateValidators.class))
//        .thenReturn(mockDataForm);
//    when(mockDataForm.bind(any(JsonNode.class))).thenReturn(mockForm);
//    when(mockForm.hasErrors()).thenReturn(false);
//
//    // ACT
//    Either<Map<String, List<ValidationError>>, Genre> genreOrError = genreService
//        .update(savedGenre, newGenre);
//
//    // ASSERT
//    // assert left (error value) is not present
//    assertFalse(genreOrError.isLeft());
//    // assert right (success value) is present
//    assertTrue(genreOrError.isRight());
//    // verify that the user repository updated the user
//    ArgumentCaptor<Genre> argument = ArgumentCaptor.forClass(Genre.class);
//    verify(mockGenreRepository).update(argument.capture());
//    assertThat(argument.getValue().getName(), is("Deep House"));
//  }

}
