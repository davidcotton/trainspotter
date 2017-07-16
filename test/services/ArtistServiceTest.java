package services;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.PersistenceException;
import io.atlassian.fugue.Either;
import models.Artist;
import models.Artist.Status;
import models.create.CreateArtist;
import models.update.UpdateArtist;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import play.data.Form;
import repositories.ArtistRepository;

@RunWith(MockitoJUnitRunner.class)
public class ArtistServiceTest {

  @InjectMocks private ArtistService artistService;
  @Mock private ArtistRepository mockArtistRepository;
  @Mock private Form<CreateArtist> mockCreateArtistForm;
  @Mock private Form<UpdateArtist> mockUpdateArtistForm;

  @Test public void fetchAll() {
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

  @Test public void findById_givenIdInDb() {
    // ARRANGE
    long id = 1L;
    when(mockArtistRepository.findById(id)).thenReturn(Optional.of(mock(Artist.class)));

    // ACT
    Optional<Artist> maybeArtist = artistService.findById(id);

    // ASSERT
    assertTrue(maybeArtist.isPresent());
  }

  @Test public void findById_givenIdNotInDb() {
    // ARRANGE
    long nonExistentId = 1L;
    when(mockArtistRepository.findById(nonExistentId)).thenReturn(Optional.empty());

    // ACT
    Optional<Artist> maybeArtist = artistService.findById(nonExistentId);

    // ASSERT
    assertFalse(maybeArtist.isPresent());
  }

  @Test public void findByName_givenNameInDb() {
    // ARRANGE
    String name = "John Digweed";
    when(mockArtistRepository.findByName(name)).thenReturn(Optional.of(mock(Artist.class)));

    // ACT
    Optional<Artist> maybeArtist = artistService.findByName(name);

    // ASSERT
    assertTrue(maybeArtist.isPresent());
  }

  @Test public void findByName_givenNameNotInDb() {
    // ARRANGE
    String name = "Cher";
    when(mockArtistRepository.findByName(name)).thenReturn(Optional.empty());

    // ACT
    Optional<Artist> maybeArtist = artistService.findByName(name);

    // ASSERT
    assertFalse(maybeArtist.isPresent());
  }

  @Test public void insert_success() {
    // ARRANGE
    CreateArtist createArtist = new CreateArtist("John Digweed", "john-digweed.jpg", "Diggers is tops.");

    when(mockCreateArtistForm.hasErrors()).thenReturn(false);
    when(mockCreateArtistForm.get()).thenReturn(createArtist);

    // ACT
    Either<Form<CreateArtist>, Artist> artistOrError = artistService.insert(mockCreateArtistForm);

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

  @Test public void insert_failureWhenFailsValidation() {
    // ARRANGE
    when(mockCreateArtistForm.hasErrors()).thenReturn(true);
    Artist mockArtist = mock(Artist.class);

    // ACT
    Either<Form<CreateArtist>, Artist> artistOrError = artistService.insert(mockCreateArtistForm);

    // ASSERT
    // assert right (success value) is not present
    assertFalse(artistOrError.isRight());
    // assert left (error value) is present
    assertTrue(artistOrError.isLeft());
    // verify that the artistRepository never tried to insert the invalid artist
    verify(mockArtistRepository, never()).insert(mockArtist);
  }

  @Test public void update_success() {
    // ARRANGE
    Artist savedArtist = new Artist(
        1L, "John Digweed", "john-digweed", "image.jpg", "description", null, null,
        null, null, null, null, ZonedDateTime.now(), ZonedDateTime.now()
    );
    UpdateArtist updateArtist = new UpdateArtist("Richie Hawtin", "new-image.jpg", "new description");

    when(mockUpdateArtistForm.hasErrors()).thenReturn(false);
    when(mockUpdateArtistForm.get()).thenReturn(updateArtist);

    // ACT
    Either<Form<UpdateArtist>, Artist> artistOrError = artistService.update(savedArtist, mockUpdateArtistForm);

    // ASSERT
    // assert left (error value) is not present
    assertFalse(artistOrError.isLeft());
    // assert right (success value) is present
    assertTrue(artistOrError.isRight());
    // verify that the user repository updated the user
    ArgumentCaptor<Artist> argument = ArgumentCaptor.forClass(Artist.class);
    verify(mockArtistRepository).update(argument.capture());
    assertThat(argument.getValue().getName(), is("Richie Hawtin"));
  }

  @Test public void update_failureWhenFailsValidation() {
    // ARRANGE
    Artist savedArtist = new Artist(
        1L, "John Digweed", "john-digweed", "image.jpg", "description", null, null,
        null, null, null, null, ZonedDateTime.now(), ZonedDateTime.now()
    );
    Artist mockArtist = mock(Artist.class);

    when(mockUpdateArtistForm.hasErrors()).thenReturn(true);

    // ACT
    Either<Form<UpdateArtist>, Artist> artistOrError = artistService.update(savedArtist, mockUpdateArtistForm);

    // ASSERT
    // assert right (success value) is not present
    assertFalse(artistOrError.isRight());
    // assert left (error value) is present
    assertTrue(artistOrError.isLeft());
    // verify that the artistRepository never tried to insert the invalid artist
    verify(mockArtistRepository, never()).update(mockArtist);
  }

//  @Test public void delete_success() throws Exception {
//    // ARRANGE
//    Artist artist = new Artist(
//        1L, "John Digweed", "john-digweed", null, null, null, null,
//        null, null, null, null, ZonedDateTime.now(), ZonedDateTime.now()
//    );
//    //when(mockArtistRepository.update(artist)).
//    doThrow(new PersistenceException()).doNothing().when(mockArtistRepository).update(artist);
//
//    // ACT
//    artist.delete();
//
//    // ASSERT
//    // assert the artist has been soft-deleted
//    assertThat(artist.getStatus(), is(Status.deleted));
//    // verify that the changes have been persisted
//    verify(mockArtistRepository).update(artist);
//  }
}
