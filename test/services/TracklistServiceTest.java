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

import models.Tracklist;

import org.hamcrest.collection.IsEmptyCollection;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import play.data.FormFactory;

import repositories.TracklistRepository;

@RunWith(MockitoJUnitRunner.class)
public class TracklistServiceTest {

  @InjectMocks
  private TracklistService tracklistService;

  @Mock
  private TracklistRepository mockTracklistRepository;

  @Mock
  private FormFactory mockFormFactory;

  @Test
  public void findAll() {
    // ARRANGE
    when(mockTracklistRepository.findAll()).thenReturn(new ArrayList<Tracklist>() {{
      add(mock(Tracklist.class));
      add(mock(Tracklist.class));
    }});

    // ACT
    List<Tracklist> actualTracklists = tracklistService.fetchAll();

    // ASSERT
    assertThat(actualTracklists, not(IsEmptyCollection.empty()));
    assertThat(actualTracklists.size(), is(2));
  }

  @Test
  public void findById_givenIdInDb() {
    // ARRANGE
    long id = 1L;
    when(mockTracklistRepository.findById(id)).thenReturn(Optional.of(mock(Tracklist.class)));

    // ACT
    Optional<Tracklist> maybeTracklist = tracklistService.findById(id);

    // ASSERT
    assertTrue(maybeTracklist.isPresent());
  }

  @Test
  public void findById_givenIdNotInDb() {
    // ARRANGE
    long nonExistentId = 1L;
    when(mockTracklistRepository.findById(nonExistentId)).thenReturn(Optional.empty());

    // ACT
    Optional<Tracklist> maybeTracklist = tracklistService.findById(nonExistentId);

    // ASSERT
    assertThat(maybeTracklist.isPresent(), is(false));
  }

}
