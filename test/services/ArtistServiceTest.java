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

import models.Artist;

import org.hamcrest.collection.IsEmptyCollection;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import play.data.FormFactory;

import repositories.ArtistRepository;

@RunWith(MockitoJUnitRunner.class)
public class ArtistServiceTest {
  
  @InjectMocks
  private ArtistService artistService;
  
  @Mock
  private ArtistRepository mockArtistRepository;
  
  @Mock
  private FormFactory mockFormFactory;

  @Test
  public void findAll() {
    // ARRANGE
    when(mockArtistRepository.findAll()).thenReturn(new ArrayList<Artist>() {{
      add(mock(Artist.class));
      add(mock(Artist.class));
    }});

    // ACT
    List<Artist> actualArtists = artistService.fetchAll();

    // ASSERT
    assertThat(actualArtists, not(IsEmptyCollection.empty()));
    assertThat(actualArtists.size(), is(2));
  }

  @Test
  public void findById_givenIdInDb() {
    // ARRANGE
    long id = 1L;
    when(mockArtistRepository.findById(id)).thenReturn(Optional.of(mock(Artist.class)));

    // ACT
    Optional<Artist> maybeArtist = artistService.findById(id);

    // ASSERT
    assertTrue(maybeArtist.isPresent());
  }

  @Test
  public void findById_givenIdNotInDb() {
    // ARRANGE
    long nonExistentId = 1L;
    when(mockArtistRepository.findById(nonExistentId)).thenReturn(Optional.empty());

    // ACT
    Optional<Artist> maybeArtist = artistService.findById(nonExistentId);

    // ASSERT
    assertThat(maybeArtist.isPresent(), is(false));
  }

  @Test
  public void findByName_givenNameInDb() {
    // ARRANGE
    String name = "John Digweed";
    when(mockArtistRepository.findByName(name)).thenReturn(Optional.of(mock(Artist.class)));

    // ACT
    Optional<Artist> maybeArtist = artistService.findByName(name);

    // ASSERT
    assertTrue(maybeArtist.isPresent());
  }

  @Test
  public void findByName_givenNameNotInDb() {
    // ARRANGE
    String name = "Cher";
    when(mockArtistRepository.findByName(name)).thenReturn(Optional.empty());

    // ACT
    Optional<Artist> maybeArtist = artistService.findByName(name);

    // ASSERT
    assertThat(maybeArtist.isPresent(), is(false));
  }

}
