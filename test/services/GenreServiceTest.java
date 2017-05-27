package services;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;

import static org.junit.Assert.assertTrue;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import models.Genre;

import org.hamcrest.collection.IsEmptyCollection;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import play.data.FormFactory;

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
    String name = "Techno";
    when(mockGenreRepository.findByName(name)).thenReturn(Optional.of(mock(Genre.class)));

    // ACT
    Optional<Genre> maybeGenre = genreService.findByName(name);

    // ASSERT
    assertTrue(maybeGenre.isPresent());
  }

  @Test
  public void findByName_givenNameNotInDb() {
    // ARRANGE
    String name = "Classical Jazz";
    when(mockGenreRepository.findByName(name)).thenReturn(Optional.empty());

    // ACT
    Optional<Genre> maybeGenre = genreService.findByName(name);

    // ASSERT
    assertThat(maybeGenre.isPresent(), is(false));
  }

}
