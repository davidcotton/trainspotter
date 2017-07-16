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

import io.atlassian.fugue.Either;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import models.Channel;
import models.Program;
import models.Program.Status;
import models.create.CreateProgram;
import models.update.UpdateProgram;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import play.data.Form;
import repositories.ProgramRepository;

@RunWith(MockitoJUnitRunner.class)
public class ProgramServiceTest {

  @InjectMocks private ProgramService programService;
  @Mock private ProgramRepository mockProgramRepository;
  @Mock private Form<CreateProgram> mockCreateProgramForm;
  @Mock private Form<UpdateProgram> mockUpdateProgramForm;

  @Test public void fetchAll() {
    // ARRANGE
    when(mockProgramRepository.findAll()).thenReturn(new ArrayList<Program>() {{
      add(mock(Program.class));
      add(mock(Program.class));
    }});

    // ACT
    List<Program> actualPrograms = programService.fetchAll();

    // ASSERT
    assertThat(actualPrograms, not(IsEmptyCollection.empty()));
    assertThat(actualPrograms.size(), is(2));
  }

  @Test public void findById_whenIdInDb() {
    // ARRANGE
    long id = 1L;
    when(mockProgramRepository.findById(id)).thenReturn(Optional.of(mock(Program.class)));

    // ACT
    Optional<Program> maybeProgram = programService.findById(id);

    // ASSERT
    assertTrue(maybeProgram.isPresent());
  }

  @Test public void findById_whenIdNotInDb() {
    // ARRANGE
    long nonExistentId = 1L;
    when(mockProgramRepository.findById(nonExistentId)).thenReturn(Optional.empty());

    // ACT
    Optional<Program> maybeProgram = programService.findById(nonExistentId);

    // ASSERT
    assertFalse(maybeProgram.isPresent());
  }

  @Test public void findByName_whenNameInDb() {
    // ARRANGE
    String name = "Transitions";
    when(mockProgramRepository.findByName(name)).thenReturn(Optional.of(mock(Program.class)));

    // ACT
    Optional<Program> maybeProgram = programService.findByName(name);

    // ASSERT
    assertTrue(maybeProgram.isPresent());
  }

  @Test public void findByName_whenNameNotInDb() {
    // ARRANGE
    String name = "Full Metal Racket";
    when(mockProgramRepository.findByName(name)).thenReturn(Optional.empty());

    // ACT
    Optional<Program> maybeProgram = programService.findByName(name);

    // ASSERT
    assertFalse(maybeProgram.isPresent());
  }

  @Test public void findBySlug_whenSlugInDb() {
    // ARRANGE
    String name = "transitions";
    when(mockProgramRepository.findByName(name)).thenReturn(Optional.of(mock(Program.class)));

    // ACT
    Optional<Program> maybeProgram = programService.findByName(name);

    // ASSERT
    assertTrue(maybeProgram.isPresent());
  }

  @Test public void findBySlug_whenSlugNotInDb() {
    // ARRANGE
    String name = "full-metal-racket";
    when(mockProgramRepository.findByName(name)).thenReturn(Optional.empty());

    // ACT
    Optional<Program> maybeProgram = programService.findByName(name);

    // ASSERT
    assertFalse(maybeProgram.isPresent());
  }

  @Test public void insert_success() {
    // ARRANGE
    Channel channel = mock(Channel.class);
    CreateProgram createProgram = new CreateProgram("Transitions", "transitions.jpg", "Description", channel);

    when(mockCreateProgramForm.hasErrors()).thenReturn(false);
    when(mockCreateProgramForm.get()).thenReturn(createProgram);

    // ACT
    Either<Form<CreateProgram>, Program> programOrError = programService.insert(mockCreateProgramForm);

    // ASSERT
    // assert left (error value) is not present
    assertFalse(programOrError.isLeft());
    // assert right (success value) is present
    assertTrue(programOrError.isRight());
    assertThat(programOrError.right().get(), instanceOf(Program.class));
    // verify that the program repository inserted the new program
    ArgumentCaptor<Program> argument = ArgumentCaptor.forClass(Program.class);
    verify(mockProgramRepository).insert(argument.capture());
    assertThat(argument.getValue().getName(), is("Transitions"));
  }

  @Test public void insert_failureWhenFailsValidation() {
    // ARRANGE
    when(mockCreateProgramForm.hasErrors()).thenReturn(true);
    Program mockProgram = mock(Program.class);

    // ACT
    Either<Form<CreateProgram>, Program> programOrError = programService.insert(mockCreateProgramForm);

    // ASSERT
    // assert right (success value) is not present
    assertFalse(programOrError.isRight());
    // assert left (error value) is present
    assertTrue(programOrError.isLeft());
    // verify that the programRepository never tried to insert the invalid program
    verify(mockProgramRepository, never()).insert(mockProgram);
  }

  @Test public void update_success() {
    // ARRANGE
    Channel channel = mock(Channel.class);
    Program savedProgram = new Program(
        1L, "Transitions", "transitions", "image.jpg", "description",
        channel, null, Status.active, ZonedDateTime.now(), ZonedDateTime.now()
    );
    UpdateProgram updateProgram = new UpdateProgram("Drumcode", "new-image.jpg", "new description", channel);

    when(mockUpdateProgramForm.hasErrors()).thenReturn(false);
    when(mockUpdateProgramForm.get()).thenReturn(updateProgram);

    // ACT
    Either<Form<UpdateProgram>, Program> programOrError = programService.update(savedProgram, mockUpdateProgramForm);

    // ASSERT
    // assert left (error value) is not present
    assertFalse(programOrError.isLeft());
    // assert right (success value) is present
    assertTrue(programOrError.isRight());
    // verify that the user repository updated the user
    ArgumentCaptor<Program> argument = ArgumentCaptor.forClass(Program.class);
    verify(mockProgramRepository).update(argument.capture());
    assertThat(argument.getValue().getName(), is("Drumcode"));
  }

  @Test public void update_failureWhenFailsValidation() {
    // ARRANGE
    Channel channel = mock(Channel.class);
    Program savedProgram = new Program(
        1L, "Transitions", "transitions", "image.jpg", "description",
        channel, null, Status.active, ZonedDateTime.now(), ZonedDateTime.now()
    );
    Program mockProgram = mock(Program.class);

    when(mockUpdateProgramForm.hasErrors()).thenReturn(true);

    // ACT
    Either<Form<UpdateProgram>, Program> programOrError = programService.update(savedProgram, mockUpdateProgramForm);

    // ASSERT
    // assert right (success value) is not present
    assertFalse(programOrError.isRight());
    // assert left (error value) is present
    assertTrue(programOrError.isLeft());
    // verify that the programRepository never tried to insert the invalid program
    verify(mockProgramRepository, never()).update(mockProgram);
  }
}
