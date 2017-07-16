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
import models.Tracklist;
import models.Tracklist.Status;
import models.User;
import models.create.CreateTracklist;
import models.update.UpdateTracklist;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import play.data.Form;
import repositories.TracklistRepository;

@RunWith(MockitoJUnitRunner.class)
public class TracklistServiceTest {

  @InjectMocks private TracklistService tracklistService;
  @Mock private TracklistRepository mockTracklistRepository;
  @Mock private UserService mockUserService;
  @Mock private Form<CreateTracklist> mockCreateTracklistForm;
  @Mock private Form<UpdateTracklist> mockUpdateTracklistForm;

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
    assertFalse(maybeTracklist.isPresent());
  }

  @Test public void insert_success() {
    // ARRANGE
    CreateTracklist createTracklist = new CreateTracklist("Transitions 657", LocalDate.now(), "", null);
    User user = new User(
        1L, "guy.incognito@simpsons.com", "Guy Incognito", "guy-incognito", User.Status.active,
        0, 0, 0, "$2a$16$MdmQnCqlTMk8hXUSWM8QCONuztBiiV9DIglZfuyxwjNknA3yENMOK",
        null, null, null, null, ZonedDateTime.now(), ZonedDateTime.now()
    );

    when(mockCreateTracklistForm.hasErrors()).thenReturn(false);
    when(mockCreateTracklistForm.get()).thenReturn(createTracklist);

    // ACT
    Either<Form<CreateTracklist>, Tracklist> tracklistOrError = tracklistService.insert(mockCreateTracklistForm, user);

    // ASSERT
    // assert left (error value) is not present
    assertFalse(tracklistOrError.isLeft());
    // assert right (success value) is present
    assertTrue(tracklistOrError.isRight());
    assertThat(tracklistOrError.right().get(), instanceOf(Tracklist.class));
    // verify that the tracklist repository inserted the new tracklist
    ArgumentCaptor<Tracklist> argument = ArgumentCaptor.forClass(Tracklist.class);
    verify(mockTracklistRepository).insert(argument.capture());
    assertThat(argument.getValue().getName(), is("Transitions 657"));
    // verify that the received credit for posting
    verify(mockUserService).addTracklist(user);
  }

  @Test public void insert_failureWhenFailsValidation() {
    // ARRANGE
    when(mockCreateTracklistForm.hasErrors()).thenReturn(true);
    Tracklist mockTracklist = mock(Tracklist.class);
    User mockUser = mock(User.class);

    // ACT
    Either<Form<CreateTracklist>, Tracklist> tracklistOrError = tracklistService.insert(mockCreateTracklistForm, mockUser);

    // ASSERT
    // assert right (success value) is not present
    assertFalse(tracklistOrError.isRight());
    // assert left (error value) is present
    assertTrue(tracklistOrError.isLeft());
    // verify that the tracklistRepository never tried to insert the invalid tracklist
    verify(mockTracklistRepository, never()).insert(mockTracklist);
    // verify the user never received credit
    verify(mockUserService, never()).addTracklist(mockUser);
  }

  @Test public void update_success() {
    // ARRANGE
    User user = new User(
        1L, "guy.incognito@simpsons.com", "Guy Incognito", "guy-incognito", User.Status.active,
        0, 0, 0, "$2a$16$MdmQnCqlTMk8hXUSWM8QCONuztBiiV9DIglZfuyxwjNknA3yENMOK",
        null, null, null, null, ZonedDateTime.now(), ZonedDateTime.now()
    );
    Tracklist savedTracklist = new Tracklist(
        1L, "Transitions 657", "transitions-657", LocalDate.now(), "image.jpg", user,
        null, null, null, null, Status.active, ZonedDateTime.now(), ZonedDateTime.now()
    );
    UpdateTracklist updateTracklist = new UpdateTracklist("Transitions 658", LocalDate.now().plusDays(2), "transitions.jpg", null);

    when(mockUpdateTracklistForm.hasErrors()).thenReturn(false);
    when(mockUpdateTracklistForm.get()).thenReturn(updateTracklist);

    // ACT
    Either<Form<UpdateTracklist>, Tracklist> tracklistOrError = tracklistService.update(savedTracklist, mockUpdateTracklistForm);

    // ASSERT
    // assert left (error value) is not present
    assertFalse(tracklistOrError.isLeft());
    // assert right (success value) is present
    assertTrue(tracklistOrError.isRight());
    // verify that the user repository updated the user
    ArgumentCaptor<Tracklist> argument = ArgumentCaptor.forClass(Tracklist.class);
    verify(mockTracklistRepository).update(argument.capture());
    assertThat(argument.getValue().getName(), is("Transitions 658"));
  }

  @Test public void update_failureWhenFailsValidation() {
    // ARRANGE
    User user = new User(
        1L, "guy.incognito@simpsons.com", "Guy Incognito", "guy-incognito", User.Status.active,
        0, 0, 0, "$2a$16$MdmQnCqlTMk8hXUSWM8QCONuztBiiV9DIglZfuyxwjNknA3yENMOK",
        null, null, null, null, ZonedDateTime.now(), ZonedDateTime.now()
    );
    Tracklist savedTracklist = new Tracklist(
        1L, "Transitions 657", "transitions-657", LocalDate.now(), "image.jpg", user,
        null, null, null, null, Status.active, ZonedDateTime.now(), ZonedDateTime.now()
    );
    Tracklist mockTracklist = mock(Tracklist.class);

    when(mockUpdateTracklistForm.hasErrors()).thenReturn(true);

    // ACT
    Either<Form<UpdateTracklist>, Tracklist> tracklistOrError = tracklistService.update(savedTracklist, mockUpdateTracklistForm);

    // ASSERT
    // assert right (success value) is not present
    assertFalse(tracklistOrError.isRight());
    // assert left (error value) is present
    assertTrue(tracklistOrError.isLeft());
    // verify that the tracklistRepository never tried to insert the invalid tracklist
    verify(mockTracklistRepository, never()).update(mockTracklist);
  }
  
}
