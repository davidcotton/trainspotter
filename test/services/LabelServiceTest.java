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

import models.Label;

import org.hamcrest.collection.IsEmptyCollection;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import play.data.FormFactory;

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
}
