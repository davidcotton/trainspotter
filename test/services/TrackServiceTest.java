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

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import io.atlassian.fugue.Either;
import models.Track;
import models.Track.Status;
import models.create.CreateTrack;
import models.update.UpdateTrack;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import play.data.Form;
import repositories.TrackRepository;

@RunWith(MockitoJUnitRunner.class)
public class TrackServiceTest {

  @InjectMocks private TrackService trackService;
  @Mock private TrackRepository mockTrackRepository;
  @Mock private Form<CreateTrack> mockCreateTrackForm;
  @Mock private Form<UpdateTrack> mockUpdateTrackForm;

  @Test public void fetchAll() {
    // ARRANGE
    when(mockTrackRepository.findAll()).thenReturn(new ArrayList<Track>() {{
      add(mock(Track.class));
      add(mock(Track.class));
    }});

    // ACT
    List<Track> actualTracks = trackService.fetchAll();

    // ASSERT
    assertThat(actualTracks, not(IsEmptyCollection.empty()));
    assertThat(actualTracks.size(), is(2));
  }

  @Test public void findById_givenIdInDb() {
    // ARRANGE
    long id = 1L;
    when(mockTrackRepository.findById(id)).thenReturn(Optional.of(mock(Track.class)));

    // ACT
    Optional<Track> maybeTrack = trackService.findById(id);

    // ASSERT
    assertTrue(maybeTrack.isPresent());
  }

  @Test public void findById_givenIdNotInDb() {
    // ARRANGE
    long nonExistentId = 1L;
    when(mockTrackRepository.findById(nonExistentId)).thenReturn(Optional.empty());

    // ACT
    Optional<Track> maybeTrack = trackService.findById(nonExistentId);

    // ASSERT
    assertFalse(maybeTrack.isPresent());
  }

  @Test public void findByName_givenNameInDb() {
    // ARRANGE
    String trackName = "Beautiful Strange";
    when(mockTrackRepository.findByName(trackName)).thenReturn(Optional.of(mock(Track.class)));

    // ACT
    Optional<Track> maybeTrack = trackService.findByName(trackName);

    // ASSERT
    assertTrue(maybeTrack.isPresent());
  }

  @Test public void findByName_givenNameNotInDb() {
    // ARRANGE
    String trackName = "Ice Ice Baby";
    when(mockTrackRepository.findByName(trackName)).thenReturn(Optional.empty());

    // ACT
    Optional<Track> maybeTrack = trackService.findByName(trackName);

    // ASSERT
    assertFalse(maybeTrack.isPresent());
  }

  @Test public void insert_success() {
    // ARRANGE
    CreateTrack createTrack = new CreateTrack("Heaven Scent", null, null, "", null, null, LocalDate.now());

    when(mockCreateTrackForm.hasErrors()).thenReturn(false);
    when(mockCreateTrackForm.get()).thenReturn(createTrack);

    // ACT
    Either<Form<CreateTrack>, Track> trackOrError = trackService.insert(mockCreateTrackForm);

    // ASSERT
    // assert left (error value) is not present
    assertFalse(trackOrError.isLeft());
    // assert right (success value) is present
    assertTrue(trackOrError.isRight());
    assertThat(trackOrError.right().get(), instanceOf(Track.class));
    // verify that the track repository inserted the new track
    ArgumentCaptor<Track> argument = ArgumentCaptor.forClass(Track.class);
    verify(mockTrackRepository).insert(argument.capture());
    assertThat(argument.getValue().getName(), is("Heaven Scent"));
  }

  @Test public void insert_failureWhenFailsValidation() {
    // ARRANGE
    when(mockCreateTrackForm.hasErrors()).thenReturn(true);
    Track mockTrack = mock(Track.class);

    // ACT
    Either<Form<CreateTrack>, Track> trackOrError = trackService.insert(mockCreateTrackForm);

    // ASSERT
    // assert right (success value) is not present
    assertFalse(trackOrError.isRight());
    // assert left (error value) is present
    assertTrue(trackOrError.isLeft());
    // verify that the trackRepository never tried to insert the invalid track
    verify(mockTrackRepository, never()).insert(mockTrack);
  }

  @Test public void update_success() {
    // ARRANGE
    Track savedTrack = new Track(
        1L, "Heaven Scent", null, null, "", null, null,
        LocalDate.now(), null, Status.active, ZonedDateTime.now(), ZonedDateTime.now()
    );
    UpdateTrack updateTrack = new UpdateTrack("Xpander", null, null, "", null, null, LocalDate.now().minusMonths(-2));

    when(mockUpdateTrackForm.hasErrors()).thenReturn(false);
    when(mockUpdateTrackForm.get()).thenReturn(updateTrack);

    // ACT
    Either<Form<UpdateTrack>, Track> trackOrError = trackService.update(savedTrack, mockUpdateTrackForm);

    // ASSERT
    // assert left (error value) is not present
    assertFalse(trackOrError.isLeft());
    // assert right (success value) is present
    assertTrue(trackOrError.isRight());
    // verify that the user repository updated the user
    ArgumentCaptor<Track> argument = ArgumentCaptor.forClass(Track.class);
    verify(mockTrackRepository).update(argument.capture());
    assertThat(argument.getValue().getName(), is("Xpander"));
  }

  @Test public void update_failureWhenFailsValidation() {
    // ARRANGE
    Track savedTrack = new Track(
        1L, "Heaven Scent", null, null, "", null, null,
        LocalDate.now(), null, Status.active, ZonedDateTime.now(), ZonedDateTime.now()
    );
    Track mockTrack = mock(Track.class);

    when(mockUpdateTrackForm.hasErrors()).thenReturn(true);

    // ACT
    Either<Form<UpdateTrack>, Track> trackOrError = trackService.update(savedTrack, mockUpdateTrackForm);

    // ASSERT
    // assert right (success value) is not present
    assertFalse(trackOrError.isRight());
    // assert left (error value) is present
    assertTrue(trackOrError.isLeft());
    // verify that the trackRepository never tried to insert the invalid track
    verify(mockTrackRepository, never()).update(mockTrack);
  }

}
