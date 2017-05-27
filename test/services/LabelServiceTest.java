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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import io.atlassian.fugue.Either;
import models.Label;

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
import repositories.LabelRepository;

@RunWith(MockitoJUnitRunner.class)
public class LabelServiceTest {

  @InjectMocks
  private LabelService labelService;

  @Mock
  private LabelRepository mockLabelRepository;

  @Mock
  private FormFactory mockFormFactory;

  @Test
  public void fetchAll() {
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

  @Test
  public void findById_givenIdInDb() {
    // ARRANGE
    long id = 1L;
    when(mockLabelRepository.findById(id)).thenReturn(Optional.of(mock(Label.class)));

    // ACT
    Optional<Label> maybeLabel = labelService.findById(id);

    // ASSERT
    assertTrue(maybeLabel.isPresent());
  }

  @Test
  public void findById_givenIdNotInDb() {
    // ARRANGE
    long nonExistentId = 1L;
    when(mockLabelRepository.findById(nonExistentId)).thenReturn(Optional.empty());

    // ACT
    Optional<Label> maybeLabel = labelService.findById(nonExistentId);

    // ASSERT
    assertThat(maybeLabel.isPresent(), is(false));
  }

  @Test
  public void findByName_givenNameInDb() {
    // ARRANGE
    String name = "Bedrock Records";
    when(mockLabelRepository.findByName(name)).thenReturn(Optional.of(mock(Label.class)));

    // ACT
    Optional<Label> maybeLabel = labelService.findByName(name);

    // ASSERT
    assertTrue(maybeLabel.isPresent());
  }

  @Test
  public void findByName_givenNameNotInDb() {
    // ARRANGE
    String name = "Def Jam";
    when(mockLabelRepository.findByName(name)).thenReturn(Optional.empty());

    // ACT
    Optional<Label> maybeLabel = labelService.findByName(name);

    // ASSERT
    assertThat(maybeLabel.isPresent(), is(false));
  }

  @Test
  public void insert_successGivenValidData() {
    // ARRANGE
    Label label = new Label(null, "Bedrock Records", null, null, null, null, null);

    Form mockForm = mock(Form.class);
    Form mockDataForm = mock(Form.class);

    when(mockFormFactory.form(Label.class, Label.InsertValidators.class)).thenReturn(mockDataForm);
    when(mockDataForm.bind(any(JsonNode.class))).thenReturn(mockForm);
    when(mockForm.hasErrors()).thenReturn(false);

    // ACT
    Either<Map<String, List<ValidationError>>, Label> labelOrError = labelService.insert(label);

    // ASSERT
    // assert left (error value) is not present
    assertFalse(labelOrError.isLeft());
    // assert right (success value) is present
    assertTrue(labelOrError.isRight());
    assertThat(labelOrError.right().get(), instanceOf(Label.class));
    // verify that the user repository inserted the new user
    ArgumentCaptor<Label> argument = ArgumentCaptor.forClass(Label.class);
    verify(mockLabelRepository).insert(argument.capture());
    assertThat(argument.getValue().getName(), is("Bedrock Records"));
  }

  @Test
  public void insert_failureGivenInvalidData() {
    // ARRANGE
    Label label = new Label(null, null, null, null, null, null, null);

    Map<String, List<ValidationError>> validationErrors =
        new HashMap<String, List<ValidationError>>() {{
          put("name", mock(List.class));
        }};

    Form mockForm = mock(Form.class);
    Form mockDataForm = mock(Form.class);

    when(mockFormFactory.form(Label.class, Label.InsertValidators.class)).thenReturn(mockDataForm);
    when(mockDataForm.bind(any(JsonNode.class))).thenReturn(mockForm);
    when(mockForm.hasErrors()).thenReturn(true);
    when(mockForm.errors()).thenReturn(validationErrors);

    // ACT
    Either<Map<String, List<ValidationError>>, Label> labelOrError = labelService.insert(label);

    // ASSERT
    // assert right (success value) is not present
    assertFalse(labelOrError.isRight());
    // assert left (error value) is present
    assertTrue(labelOrError.isLeft());
    assertThat(labelOrError.left().get().get("name"), instanceOf(List.class));
    // verify that the labelRepository never tried to insert the invalid label
    verify(mockLabelRepository, never()).insert(any());
  }


}
