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

import models.Artist;

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
  public void fetchAll() {
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

  @Test
  public void insert_successGivenValidData() {
    // ARRANGE
    Artist artist = new Artist(null, "John Digweed", null, null, null, null, null, null, null);

    Form mockForm = mock(Form.class);
    Form mockDataForm = mock(Form.class);

    when(mockFormFactory.form(Artist.class, Artist.InsertValidators.class)).thenReturn(mockDataForm);
    when(mockDataForm.bind(any(JsonNode.class))).thenReturn(mockForm);
    when(mockForm.hasErrors()).thenReturn(false);

    // ACT
    Either<Map<String, List<ValidationError>>, Artist> artistOrError = artistService.insert(artist);

    // ASSERT
    // assert left (error value) is not present
    assertFalse(artistOrError.isLeft());
    // assert right (success value) is present
    assertTrue(artistOrError.isRight());
    assertThat(artistOrError.right().get(), instanceOf(Artist.class));
    // verify that the artist repository inserted the new artist
    ArgumentCaptor<Artist> argument = ArgumentCaptor.forClass(Artist.class);
    verify(mockArtistRepository).insert(argument.capture());
    assertThat(argument.getValue().getName(), is("John Digweed"));
  }

  @Test
  public void insert_failureGivenInvalidData() {
    // ARRANGE
    Artist artist = new Artist(null, null, null, null, null, null, null, null, null);

    Map<String, List<ValidationError>> validationErrors =
        new HashMap<String, List<ValidationError>>() {{
          put("name", mock(List.class));
        }};

    Form mockForm = mock(Form.class);
    Form mockDataForm = mock(Form.class);

    when(mockFormFactory.form(Artist.class, Artist.InsertValidators.class)).thenReturn(mockDataForm);
    when(mockDataForm.bind(any(JsonNode.class))).thenReturn(mockForm);
    when(mockForm.hasErrors()).thenReturn(true);
    when(mockForm.errors()).thenReturn(validationErrors);

    // ACT
    Either<Map<String, List<ValidationError>>, Artist> artistOrError = artistService.insert(artist);

    // ASSERT
    // assert right (success value) is not present
    assertFalse(artistOrError.isRight());
    // assert left (error value) is present
    assertTrue(artistOrError.isLeft());
    assertThat(artistOrError.left().get().get("name"), instanceOf(List.class));
    // verify that the artistRepository never tried to insert the invalid artist
    verify(mockArtistRepository, never()).insert(any());
  }

}
