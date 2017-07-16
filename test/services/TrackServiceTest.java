package services;

import com.fasterxml.jackson.databind.JsonNode;

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

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.Map;
import java.util.Optional;

import io.atlassian.fugue.Either;
import models.Genre;
import models.Label;
import models.Track;

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
import repositories.TrackRepository;

@RunWith(MockitoJUnitRunner.class)
public class TrackServiceTest {

  @InjectMocks private TrackService trackService;
  @Mock private TrackRepository mockTrackRepository;
  @Mock private FormFactory mockFormFactory;
  @Mock private Form mockForm;
  @Mock private Form mockDataForm;

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
    assertThat(maybeTrack.isPresent(), is(false));
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
    assertThat(maybeTrack.isPresent(), is(false));
  }

//  @Test
//  public void insert_successGivenValidData() {
//    // ARRANGE
//    Track track = new Track(null, "Beautiful Strange", null, null, null, null, null, null, null, null, null);
//
//    when(mockFormFactory.form(Track.class, Track.InsertValidators.class)).thenReturn(mockDataForm);
//    when(mockDataForm.bind(any(JsonNode.class))).thenReturn(mockForm);
//    when(mockForm.hasErrors()).thenReturn(false);
//
//    // ACT
//    Either<Map<String, List<ValidationError>>, Track> trackOrError = trackService.insert(track);
//
//    // ASSERT
//    // assert left (error value) is not present
//    assertFalse(trackOrError.isLeft());
//    // assert right (success value) is present
//    assertTrue(trackOrError.isRight());
//    assertThat(trackOrError.right().get(), instanceOf(Track.class));
//    // verify that the user repository inserted the new user
//    ArgumentCaptor<Track> argument = ArgumentCaptor.forClass(Track.class);
//    verify(mockTrackRepository).insert(argument.capture());
//    assertThat(argument.getValue().getName(), is("Beautiful Strange"));
//  }
//
//  @Test
//  public void insert_failureGivenInvalidData() {
//    // ARRANGE
//    Track track = new Track(null, null, null, null, null, null, null, null, null, null, null);
//
//    Map<String, List<ValidationError>> validationErrors =
//        new HashMap<String, List<ValidationError>>() {{
//          put("name", mock(List.class));
//        }};
//
//    when(mockFormFactory.form(Track.class, Track.InsertValidators.class)).thenReturn(mockDataForm);
//    when(mockDataForm.bind(any(JsonNode.class))).thenReturn(mockForm);
//    when(mockForm.hasErrors()).thenReturn(true);
//    when(mockForm.errors()).thenReturn(validationErrors);
//
//    // ACT
//    Either<Map<String, List<ValidationError>>, Track> trackOrError = trackService.insert(track);
//
//    // ASSERT
//    // assert right (success value) is not present
//    assertFalse(trackOrError.isRight());
//    // assert left (error value) is present
//    assertTrue(trackOrError.isLeft());
//    assertThat(trackOrError.left().get().get("name"), instanceOf(List.class));
//    // verify that the trackRepository never tried to insert the invalid track
//    verify(mockTrackRepository, never()).insert(any());
//  }
//
//  @Test
//  public void update_successGivenValidData() {
//    // ARRANGE
//    Track savedTrack = new Track(
//        1L, "Heaven Scent", new ArrayList<>(), new ArrayList<>(), null,
//        mock(Genre.class), mock(Label.class), LocalDate.now(), new ArrayList<>(),
//        ZonedDateTime.now(), ZonedDateTime.now()
//    );
//    Track newTrack = new Track(
//        1L, "Beautiful Strange", new ArrayList<>(), new ArrayList<>(), null,
//        mock(Genre.class), mock(Label.class), LocalDate.now(), new ArrayList<>(),
//        ZonedDateTime.now(), ZonedDateTime.now()
//    );
//
//    when(mockFormFactory.form(Track.class, Track.UpdateValidators.class))
//        .thenReturn(mockDataForm);
//    when(mockDataForm.bind(any(JsonNode.class))).thenReturn(mockForm);
//    when(mockForm.hasErrors()).thenReturn(false);
//
//    // ACT
//    Either<Map<String, List<ValidationError>>, Track> trackOrError = trackService
//        .update(savedTrack, newTrack);
//
//    // ASSERT
//    // assert left (error value) is not present
//    assertFalse(trackOrError.isLeft());
//    // assert right (success value) is present
//    assertTrue(trackOrError.isRight());
//    // verify that the user repository updated the user
//    ArgumentCaptor<Track> argument = ArgumentCaptor.forClass(Track.class);
//    verify(mockTrackRepository).update(argument.capture());
//    assertThat(argument.getValue().getName(), is("Beautiful Strange"));
//  }


}
