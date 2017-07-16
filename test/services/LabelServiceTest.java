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

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import io.atlassian.fugue.Either;
import models.Label;
import models.Label.Status;
import models.create.CreateLabel;
import models.update.UpdateLabel;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import play.data.Form;
import repositories.LabelRepository;

@RunWith(MockitoJUnitRunner.class)
public class LabelServiceTest {

  @InjectMocks private LabelService labelService;
  @Mock private LabelRepository mockLabelRepository;
  @Mock private Form<CreateLabel> mockCreateLabelForm;
  @Mock private Form<UpdateLabel> mockUpdateLabelForm;

  @Test public void fetchAll() {
    // ARRANGE
    when(mockLabelRepository.findAll()).thenReturn(new ArrayList<Label>() {{
      add(mock(Label.class));
      add(mock(Label.class));
    }});

    // ACT
    List<Label> actualLabels = labelService.fetchAll();

    // ASSERT
    assertThat(actualLabels, not(IsEmptyCollection.empty()));
    assertThat(actualLabels.size(), is(2));
  }

  @Test public void findById_givenIdInDb() {
    // ARRANGE
    long id = 1L;
    when(mockLabelRepository.findById(id)).thenReturn(Optional.of(mock(Label.class)));

    // ACT
    Optional<Label> maybeLabel = labelService.findById(id);

    // ASSERT
    assertTrue(maybeLabel.isPresent());
  }

  @Test public void findById_givenIdNotInDb() {
    // ARRANGE
    long nonExistentId = 1L;
    when(mockLabelRepository.findById(nonExistentId)).thenReturn(Optional.empty());

    // ACT
    Optional<Label> maybeLabel = labelService.findById(nonExistentId);

    // ASSERT
    assertFalse(maybeLabel.isPresent());
  }

  @Test public void findByName_givenNameInDb() {
    // ARRANGE
    String name = "Bedrock Records";
    when(mockLabelRepository.findByName(name)).thenReturn(Optional.of(mock(Label.class)));

    // ACT
    Optional<Label> maybeLabel = labelService.findByName(name);

    // ASSERT
    assertTrue(maybeLabel.isPresent());
  }

  @Test public void findByName_givenNameNotInDb() {
    // ARRANGE
    String name = "Def Jam";
    when(mockLabelRepository.findByName(name)).thenReturn(Optional.empty());

    // ACT
    Optional<Label> maybeLabel = labelService.findByName(name);

    // ASSERT
    assertFalse(maybeLabel.isPresent());
  }

  @Test public void insert_success() {
    // ARRANGE
    CreateLabel createLabel = new CreateLabel("Bedrock Records", "bedrock-records.jpg", "Description");

    when(mockCreateLabelForm.hasErrors()).thenReturn(false);
    when(mockCreateLabelForm.get()).thenReturn(createLabel);

    // ACT
    Either<Form<CreateLabel>, Label> labelOrError = labelService.insert(mockCreateLabelForm);

    // ASSERT
    // assert left (error value) is not present
    assertFalse(labelOrError.isLeft());
    // assert right (success value) is present
    assertTrue(labelOrError.isRight());
    assertThat(labelOrError.right().get(), instanceOf(Label.class));
    // verify that the label repository inserted the new label
    ArgumentCaptor<Label> argument = ArgumentCaptor.forClass(Label.class);
    verify(mockLabelRepository).insert(argument.capture());
    assertThat(argument.getValue().getName(), is("Bedrock Records"));
  }

  @Test public void insert_failureWhenFailsValidation() {
    // ARRANGE
    when(mockCreateLabelForm.hasErrors()).thenReturn(true);
    Label mockLabel = mock(Label.class);

    // ACT
    Either<Form<CreateLabel>, Label> labelOrError = labelService.insert(mockCreateLabelForm);

    // ASSERT
    // assert right (success value) is not present
    assertFalse(labelOrError.isRight());
    // assert left (error value) is present
    assertTrue(labelOrError.isLeft());
    // verify that the labelRepository never tried to insert the invalid label
    verify(mockLabelRepository, never()).insert(mockLabel);
  }

  @Test public void update_success() {
    // ARRANGE
    Label savedLabel = new Label(
        1L, "Bedrock Records", "bedrock-records", "image.jpg", "description",
        null, null, Status.active, ZonedDateTime.now(), ZonedDateTime.now()
    );
    UpdateLabel updateLabel = new UpdateLabel("Drumcode", "drumcode.jpg", "Description");

    when(mockUpdateLabelForm.hasErrors()).thenReturn(false);
    when(mockUpdateLabelForm.get()).thenReturn(updateLabel);

    // ACT
    Either<Form<UpdateLabel>, Label> labelOrError = labelService.update(savedLabel, mockUpdateLabelForm);

    // ASSERT
    // assert left (error value) is not present
    assertFalse(labelOrError.isLeft());
    // assert right (success value) is present
    assertTrue(labelOrError.isRight());
    // verify that the user repository updated the user
    ArgumentCaptor<Label> argument = ArgumentCaptor.forClass(Label.class);
    verify(mockLabelRepository).update(argument.capture());
    assertThat(argument.getValue().getName(), is("Drumcode"));
  }

  @Test public void update_failureWhenFailsValidation() {
    // ARRANGE
    Label savedLabel = new Label(
        1L, "Bedrock Records", "bedrock-records", "image.jpg", "description",
        null, null, Status.active, ZonedDateTime.now(), ZonedDateTime.now()
    );
    Label mockLabel = mock(Label.class);

    when(mockUpdateLabelForm.hasErrors()).thenReturn(true);

    // ACT
    Either<Form<UpdateLabel>, Label> labelOrError = labelService.update(savedLabel, mockUpdateLabelForm);

    // ASSERT
    // assert right (success value) is not present
    assertFalse(labelOrError.isRight());
    // assert left (error value) is present
    assertTrue(labelOrError.isLeft());
    // verify that the labelRepository never tried to insert the invalid label
    verify(mockLabelRepository, never()).update(mockLabel);
  }
}
