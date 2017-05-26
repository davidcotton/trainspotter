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
import models.Track;

import org.hamcrest.collection.IsEmptyCollection;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import play.data.FormFactory;

import repositories.TrackRepository;

@RunWith(MockitoJUnitRunner.class)
public class TrackServiceTest {

  @InjectMocks
  private TrackService trackService;

  @Mock
  private TrackRepository mockTrackRepository;

  @Mock
  private FormFactory mockFormFactory;

  @Test
  public void findAll() {
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

  @Test
  public void findById_givenIdInDb() {
    // ARRANGE
    long id = 1L;
    when(mockTrackRepository.findById(id)).thenReturn(Optional.of(mock(Track.class)));

    // ACT
    Optional<Track> maybeTrack = trackService.findById(id);

    // ASSERT
    assertTrue(maybeTrack.isPresent());
  }

  @Test
  public void findById_givenIdNotInDb() {
    // ARRANGE
    long nonExistentId = 1L;
    when(mockTrackRepository.findById(nonExistentId)).thenReturn(Optional.empty());

    // ACT
    Optional<Track> maybeTrack = trackService.findById(nonExistentId);

    // ASSERT
    assertThat(maybeTrack.isPresent(), is(false));
  }
}
