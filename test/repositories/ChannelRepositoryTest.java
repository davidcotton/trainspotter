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
import models.Channel;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.Test;

public class ChannelRepositoryTest extends AbstractIntegrationTest {

  private ChannelRepository channelRepository;

  public ChannelRepositoryTest() {
    channelRepository = new ChannelRepository();
  }

  @Test
  public void findAll() throws Exception {
    List<Channel> channels = channelRepository.findAll();
    assertThat(channels, not(IsEmptyCollection.empty()));
    assertThat(channels.size(), is(2));
    assertThat(channels, hasItem(hasProperty("name", is("Proton Radio"))));
    assertThat(channels, hasItem(hasProperty("name", is("Triple J"))));
  }

  @Test public void findById_successGivenIdInDb() throws Exception {
    Optional<Channel> maybeChannel = channelRepository.findById(1L);
    assertTrue(maybeChannel.isPresent());
    assertEquals("Proton Radio", maybeChannel.get().getName());
  }

  @Test public void findById_failureGivenIdNotInDb() throws Exception {
    Optional<Channel> maybeChannel = channelRepository.findById(999L);
    assertFalse(maybeChannel.isPresent());
  }

  @Test public void findByEmail_successGivenEmailIdInDb() throws Exception {
    Optional<Channel> maybeChannel = channelRepository.findByName("Proton Radio");
    assertTrue(maybeChannel.isPresent());
    assertEquals("Proton Radio", maybeChannel.get().getName());
  }

  @Test public void findByEmail_failureGivenEmailNotInDb() throws Exception {
    Optional<Channel> maybeChannel = channelRepository.findByName("Nova FM");
    assertFalse(maybeChannel.isPresent());
  }

  @Test public void insert_success() throws Exception {
    // ARRANGE
    Channel channel = new Channel(null, "FBi Radio", null, null, null, null, null);

    // ACT
    channelRepository.insert(channel);

    // ASSERT
    Optional<Channel> maybeChannel = channelRepository.findByName("FBi Radio");
    assertTrue(maybeChannel.isPresent());
    assertThat(maybeChannel.get().getName(), is("FBi Radio"));
    // verify that default fields are populated
    assertNotNull(maybeChannel.get().getId());
    assertNotNull(maybeChannel.get().getCreated());
    assertNotNull(maybeChannel.get().getUpdated());
  }

  @Test public void update_success() throws Exception {
    // ARRANGE
    // fetch an channel to update
    Channel channel = channelRepository.findById(2L).orElseThrow(Exception::new);
    // update data
    channel.setName("FBi Radio");

    // ACT
    channelRepository.update(channel);

    // ASSERT
    Optional<Channel> maybeChannel = channelRepository.findByName("FBi Radio");
    assertTrue(maybeChannel.isPresent());
    // verify that the channel saved correctly
    assertThat(maybeChannel.get().getId(), is(2L));
    assertThat(maybeChannel.get().getName(), is("FBi Radio"));
    // verify that the original image field wasn't changed
    assertThat(maybeChannel.get().getImage(), is("triple-j.jpg"));
  }
}
