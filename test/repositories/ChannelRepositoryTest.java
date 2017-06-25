package repositories;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.avaje.ebean.PagedList;
import java.util.List;
import java.util.Optional;
import models.Channel;
import org.junit.Test;

public class ChannelRepositoryTest extends AbstractIntegrationTest {

  private ChannelRepository channelRepository;

  public ChannelRepositoryTest() {
    channelRepository = new ChannelRepository();
  }

  @Test public void findAll() throws Exception {
    List<Channel> channels = channelRepository.findAll();
    assertThat(channels.size(), is(3));
    // verify ordered alphabetically by name
    assertThat(channels.get(0).getName(), is("BBC Radio 1"));
    assertThat(channels.get(1).getName(), is("Proton Radio"));
    assertThat(channels.get(2).getName(), is("Triple J"));
  }

  @Test public void findAllPaged() throws Exception {
    PagedList<Channel> pagedChannels = channelRepository.findAllPaged(1);

    // verify the pagination object attributes
    assertThat(pagedChannels.getPageSize(), is(12));
    assertThat(pagedChannels.getTotalRowCount(), is(3));

    // verify the paginated list
    List<Channel> channels = pagedChannels.getList();
    assertThat(channels.get(0).getName(), is("BBC Radio 1"));
    assertThat(channels.get(1).getName(), is("Proton Radio"));
    assertThat(channels.get(2).getName(), is("Triple J"));
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

  @Test public void findByName_successGivenNameInDb() throws Exception {
    Optional<Channel> maybeChannel = channelRepository.findByName("Proton Radio");
    assertTrue(maybeChannel.isPresent());
    assertEquals("Proton Radio", maybeChannel.get().getName());
  }

  @Test public void findByName_failureGivenNameNotInDb() throws Exception {
    Optional<Channel> maybeChannel = channelRepository.findByName("Nova FM");
    assertFalse(maybeChannel.isPresent());
  }

  @Test public void findBySlug_successGivenSlugInDb() throws Exception {
    Optional<Channel> maybeChannel = channelRepository.findBySlug("proton-radio");
    assertTrue(maybeChannel.isPresent());
    assertEquals("Proton Radio", maybeChannel.get().getName());
  }

  @Test public void findBySlug_failureGivenSlugNotInDb() throws Exception {
    Optional<Channel> maybeChannel = channelRepository.findBySlug("nova-fm");
    assertFalse(maybeChannel.isPresent());
  }

  @Test public void insert_success() throws Exception {
    // ARRANGE
    Channel channel = new Channel(null, "FBi Radio", "fbi-radio", null, null, null, null, null);

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

  @Test public void delete_successGivenNoDependantPrograms() throws Exception {
    Channel channel = channelRepository.findById(3L).orElseThrow(Exception::new);
    channelRepository.delete(channel);
    Optional<Channel> maybeChannel = channelRepository.findById(3L);
    assertFalse(maybeChannel.isPresent());
  }
}
