package services;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import io.atlassian.fugue.Either;

import models.Genre;

import org.hamcrest.collection.IsEmptyCollection;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import play.data.Form;
import play.data.FormFactory;
import play.data.validation.ValidationError;

import repositories.GenreRepository;

@RunWith(MockitoJUnitRunner.class)
public class GenreServiceTest {
  
  @InjectMocks
  private GenreService genreService;
  
  @Mock
  private GenreRepository mockGenreRepository;
  
  @Mock
  private FormFactory mockFormFactory;

  @Test
  public void fetchAll() {
    // ARRANGE
    when(mockGenreRepository.findAll()).thenReturn(new ArrayList<Genre>() {{
      add(mock(Genre.class));
      add(mock(Genre.class));
    }});

    // ACT
    List<Genre> actualGenres = genreService.fetchAll();

    // ASSERT
    assertThat(actualGenres, not(IsEmptyCollection.empty()));
    assertThat(actualGenres.size(), is(2));
  }

  @Test
  public void findById_givenIdInDb() {
    // ARRANGE
    long id = 1L;
    when(mockGenreRepository.findById(id)).thenReturn(Optional.of(mock(Genre.class)));

    // ACT
    Optional<Genre> maybeGenre = genreService.findById(id);

    // ASSERT
    assertTrue(maybeGenre.isPresent());
  }

  @Test
  public void findById_givenIdNotInDb() {
    // ARRANGE
    long nonExistentId = 1L;
    when(mockGenreRepository.findById(nonExistentId)).thenReturn(Optional.empty());

    // ACT
    Optional<Genre> maybeGenre = genreService.findById(nonExistentId);

    // ASSERT
    assertThat(maybeGenre.isPresent(), is(false));
  }

  @Test
  public void findByName_givenNameInDb() {
    // ARRANGE
    String genreName = "Techno";
    when(mockGenreRepository.findByName(genreName)).thenReturn(Optional.of(mock(Genre.class)));

    // ACT
    Optional<Genre> maybeGenre = genreService.findByName(genreName);

    // ASSERT
    assertTrue(maybeGenre.isPresent());
  }

  @Test
  public void findByName_givenNameNotInDb() {
    // ARRANGE
    String genreName = "Classical Jazz";
    when(mockGenreRepository.findByName(genreName)).thenReturn(Optional.empty());

    // ACT
    Optional<Genre> maybeGenre = genreService.findByName(genreName);

    // ASSERT
    assertThat(maybeGenre.isPresent(), is(false));
  }

  @Test
  public void insert_successGivenValidData() {
    // ARRANGE
    Genre genre = new Genre(null, "Techno", null, null, null, null);

    Form mockForm = mock(Form.class);
    Form mockDataForm = mock(Form.class);

    when(mockFormFactory.form(Genre.class, Genre.InsertValidators.class)).thenReturn(mockDataForm);
    when(mockDataForm.bind(any(JsonNode.class))).thenReturn(mockForm);
    when(mockForm.hasErrors()).thenReturn(false);

    // ACT
    Either<Map<String, List<ValidationError>>, Genre> genreOrError = genreService.insert(genre);

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
  }

  @Test
  public void insert_failureGivenInvalidData() {
    // ARRANGE
    Genre genre = new Genre(null, null, null, null, null, null);

    Map<String, List<ValidationError>> validationErrors =
        new HashMap<String, List<ValidationError>>() {{
          put("name", mock(List.class));
        }};

    Form mockForm = mock(Form.class);
    Form mockDataForm = mock(Form.class);

    when(mockFormFactory.form(Genre.class, Genre.InsertValidators.class)).thenReturn(mockDataForm);
    when(mockDataForm.bind(any(JsonNode.class))).thenReturn(mockForm);
    when(mockForm.hasErrors()).thenReturn(true);
    when(mockForm.errors()).thenReturn(validationErrors);

    // ACT
    Either<Map<String, List<ValidationError>>, Genre> genreOrError = genreService.insert(genre);

    // ASSERT
    // assert right (success value) is not present
    assertFalse(genreOrError.isRight());
    // assert left (error value) is present
    assertTrue(genreOrError.isLeft());
    assertThat(genreOrError.left().get().get("name"), instanceOf(List.class));
    // verify that the genreRepository never tried to insert the invalid genre
    verify(mockGenreRepository, never()).insert(any());
  }

}
