package repositories;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;
import models.Label;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.Test;

public class LabelRepositoryTest extends AbstractIntegrationTest {
  
  private final LabelRepository labelRepository;

  public LabelRepositoryTest() {
    labelRepository = new LabelRepository();
  }

  @Test public void findAll() throws Exception {
    List<Label> labels = labelRepository.findAll();
    assertThat(labels, not(IsEmptyCollection.empty()));
    assertThat(labels.size(), is(3));
    assertThat(labels, hasItem(hasProperty("name", is("Bedrock Records"))));
    assertThat(labels, hasItem(hasProperty("name", is("Last Night On Earth"))));
    assertThat(labels, hasItem(hasProperty("name", is("Drumcode"))));
  }

  @Test public void findById_successGivenIdInDb() throws Exception {
    Optional<Label> maybeLabel = labelRepository.findById(1L);
    assertTrue(maybeLabel.isPresent());
    assertEquals("Bedrock Records", maybeLabel.get().getName());
  }

  @Test public void findById_failureGivenIdNotInDb() throws Exception {
    Optional<Label> maybeLabel = labelRepository.findById(999L);
    assertFalse(maybeLabel.isPresent());
  }

  @Test public void findByName_successGivenNameInDb() throws Exception {
    Optional<Label> maybeLabel = labelRepository.findByName("Bedrock Records");
    assertTrue(maybeLabel.isPresent());
    assertEquals("Bedrock Records", maybeLabel.get().getName());
  }

  @Test public void findByName_failureGivenNameNotInDb() throws Exception {
    Optional<Label> maybeLabel = labelRepository.findByName("Defjam");
    assertFalse(maybeLabel.isPresent());
  }

  @Test public void insert_success() throws Exception {
    // ARRANGE
    Label label = new Label(null, "Kompakt", null, null, null, null, null, null);

    // ACT
    labelRepository.insert(label);

    // ASSERT
    Optional<Label> maybeLabel = labelRepository.findByName("Kompakt");
    assertTrue(maybeLabel.isPresent());
    assertThat(maybeLabel.get().getName(), is("Kompakt"));
    // verify that default fields are populated
    assertNotNull(maybeLabel.get().getId());
    assertNotNull(maybeLabel.get().getCreated());
    assertNotNull(maybeLabel.get().getUpdated());
  }

  @Test public void update_success() throws Exception {
    // ARRANGE
    // fetch an label to update
    Label label = labelRepository.findById(3L).orElseThrow(Exception::new);
    // update data
    label.setName("Get Physical Music");

    // ACT
    labelRepository.update(label);

    // ASSERT
    Optional<Label> maybeLabel = labelRepository.findByName("Get Physical Music");
    assertTrue(maybeLabel.isPresent());
    // verify that the label saved correctly
    assertThat(maybeLabel.get().getId(), is(3L));
    assertThat(maybeLabel.get().getName(), is("Get Physical Music"));
    // verify that the original image field wasn't changed
    assertThat(maybeLabel.get().getImage(), is("drumcode.jpg"));
  }
}
