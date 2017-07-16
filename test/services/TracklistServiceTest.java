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
import models.Tracklist;
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
import repositories.TracklistRepository;

@RunWith(MockitoJUnitRunner.class)
public class TracklistServiceTest {

  @InjectMocks private TracklistService tracklistService;
  @Mock private TracklistRepository mockTracklistRepository;
  @Mock private UserService mockUserService;
  @Mock private FormFactory mockFormFactory;
  @Mock private Form mockForm;
  @Mock private Form mockDataForm;

  @Test public void fetchAll() {
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

  @Test public void findById_givenIdInDb() {
    // ARRANGE
    long id = 1L;
    when(mockTracklistRepository.findById(id)).thenReturn(Optional.of(mock(Tracklist.class)));

    // ACT
    Optional<Tracklist> maybeTracklist = tracklistService.findById(id);

    // ASSERT
    assertTrue(maybeTracklist.isPresent());
  }

  @Test public void findById_givenIdNotInDb() {
    // ARRANGE
    long nonExistentId = 1L;
    when(mockTracklistRepository.findById(nonExistentId)).thenReturn(Optional.empty());

    // ACT
    Optional<Tracklist> maybeTracklist = tracklistService.findById(nonExistentId);

    // ASSERT
    assertThat(maybeTracklist.isPresent(), is(false));
  }

//  @Test
//  public void insert_successGivenValidData() {
//    // ARRANGE
//    String tracklistName = "John Digweed & Jesper Dahlback - Transitions 664 2017-05-19";
//    Tracklist tracklist = new Tracklist(null, tracklistName, null, null, null, null, null, null, null, null);
//
//    when(mockFormFactory.form(Tracklist.class, Tracklist.InsertValidators.class)).thenReturn(mockDataForm);
//    when(mockDataForm.bind(any(JsonNode.class))).thenReturn(mockForm);
//    when(mockForm.hasErrors()).thenReturn(false);
//
//    // ACT
//    Either<Map<String, List<ValidationError>>, Tracklist> tracklistOrError = tracklistService.insert(tracklist);
//
//    // ASSERT
//    // assert left (error value) is not present
//    assertFalse(tracklistOrError.isLeft());
//    // assert right (success value) is present
//    assertTrue(tracklistOrError.isRight());
//    assertThat(tracklistOrError.right().get(), instanceOf(Tracklist.class));
//    // verify that the user repository inserted the new user
//    ArgumentCaptor<Tracklist> argument = ArgumentCaptor.forClass(Tracklist.class);
//    verify(mockTracklistRepository).insert(argument.capture());
//    assertThat(argument.getValue().getName(), is(tracklistName));
//  }
//
//  @Test
//  public void insert_failureGivenInvalidData() {
//    // ARRANGE
//    Tracklist tracklist = new Tracklist(null, null, null, null, null, null, null, null, null, null);
//
//    Map<String, List<ValidationError>> validationErrors =
//        new HashMap<String, List<ValidationError>>() {{
//          put("name", mock(List.class));
//        }};
//
//    when(mockFormFactory.form(Tracklist.class, Tracklist.InsertValidators.class)).thenReturn(mockDataForm);
//    when(mockDataForm.bind(any(JsonNode.class))).thenReturn(mockForm);
//    when(mockForm.hasErrors()).thenReturn(true);
//    when(mockForm.errors()).thenReturn(validationErrors);
//
//    // ACT
//    Either<Map<String, List<ValidationError>>, Tracklist> tracklistOrError = tracklistService.insert(tracklist);
//
//    // ASSERT
//    // assert right (success value) is not present
//    assertFalse(tracklistOrError.isRight());
//    // assert left (error value) is present
//    assertTrue(tracklistOrError.isLeft());
//    assertThat(tracklistOrError.left().get().get("name"), instanceOf(List.class));
//    // verify that the tracklistRepository never tried to insert the invalid tracklist
//    verify(mockTracklistRepository, never()).insert(any());
//  }
  
  
}
