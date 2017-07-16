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
import models.Channel;
import models.Channel.Status;
import models.create.CreateChannel;
import models.update.UpdateChannel;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import play.data.Form;
import repositories.ChannelRepository;

@RunWith(MockitoJUnitRunner.class)
public class ChannelServiceTest {
  
  @InjectMocks private ChannelService channelService;
  @Mock private ChannelRepository mockChannelRepository;
  @Mock private Form<CreateChannel> mockCreateChannelForm;
  @Mock private Form<UpdateChannel> mockUpdateChannelForm;

  @Test public void fetchAll() {
    // ARRANGE
    when(mockChannelRepository.findAll()).thenReturn(new ArrayList<Channel>() {{
      add(mock(Channel.class));
      add(mock(Channel.class));
    }});

    // ACT
    List<Channel> actualChannels = channelService.fetchAll();

    // ASSERT
    assertThat(actualChannels, not(IsEmptyCollection.empty()));
    assertThat(actualChannels.size(), is(2));
  }

  @Test public void findById_whenIdInDb() {
    long id = 1L;
    when(mockChannelRepository.findById(id)).thenReturn(Optional.of(mock(Channel.class)));
    Optional<Channel> maybeChannel = channelService.findById(id);
    assertTrue(maybeChannel.isPresent());
  }

  @Test public void findById_whenIdNotInDb() {
    long nonExistentId = 1L;
    when(mockChannelRepository.findById(nonExistentId)).thenReturn(Optional.empty());
    Optional<Channel> maybeChannel = channelService.findById(nonExistentId);
    assertFalse(maybeChannel.isPresent());
  }

  @Test public void findBySlug_whenSlugInDb() {
    String slug = "proton-radio";
    when(mockChannelRepository.findBySlug(slug)).thenReturn(Optional.of(mock(Channel.class)));
    Optional<Channel> maybeChannel = channelService.findBySlug(slug);
    assertTrue(maybeChannel.isPresent());
  }

  @Test public void findBySlug_whenSlugNotInDb() {
    String name = "nova-fm";
    when(mockChannelRepository.findBySlug(name)).thenReturn(Optional.empty());
    Optional<Channel> maybeChannel = channelService.findBySlug(name);
    assertFalse(maybeChannel.isPresent());
  }

  @Test public void insert_success() {
    // ARRANGE
    CreateChannel createChannel = new CreateChannel("FBi Radio", "fbi-radio", "Description");

    when(mockCreateChannelForm.hasErrors()).thenReturn(false);
    when(mockCreateChannelForm.get()).thenReturn(createChannel);

    // ACT
    Either<Form<CreateChannel>, Channel> channelOrError = channelService.insert(mockCreateChannelForm);

    // ASSERT
    // assert left (error value) is not present
    assertFalse(channelOrError.isLeft());
    // assert right (success value) is present
    assertTrue(channelOrError.isRight());
    assertThat(channelOrError.right().get(), instanceOf(Channel.class));
    // verify that the channel repository inserted the new channel
    ArgumentCaptor<Channel> argument = ArgumentCaptor.forClass(Channel.class);
    verify(mockChannelRepository).insert(argument.capture());
    assertThat(argument.getValue().getName(), is("FBi Radio"));
  }

  @Test public void insert_failureWhenFailsValidation() {
    // ARRANGE
    when(mockCreateChannelForm.hasErrors()).thenReturn(true);
    Channel mockChannel = mock(Channel.class);

    // ACT
    Either<Form<CreateChannel>, Channel> channelOrError = channelService.insert(mockCreateChannelForm);

    // ASSERT
    // assert right (success value) is not present
    assertFalse(channelOrError.isRight());
    // assert left (error value) is present
    assertTrue(channelOrError.isLeft());
    // verify that the channelRepository never tried to insert the invalid channel
    verify(mockChannelRepository, never()).insert(mockChannel);
  }

  @Test public void update_success() {
    // ARRANGE
    Channel savedChannel = new Channel(
        1L, "FBi Radio", "fbi-radio", "fbi-radio.jpg", "description", null,
        Status.active, ZonedDateTime.now(), ZonedDateTime.now()
    );
    UpdateChannel updateChannel = new UpdateChannel("Triple J", "new-image.jpg", "new description");

    when(mockUpdateChannelForm.hasErrors()).thenReturn(false);
    when(mockUpdateChannelForm.get()).thenReturn(updateChannel);

    // ACT
    Either<Form<UpdateChannel>, Channel> channelOrError = channelService.update(savedChannel, mockUpdateChannelForm);

    // ASSERT
    // assert left (error value) is not present
    assertFalse(channelOrError.isLeft());
    // assert right (success value) is present
    assertTrue(channelOrError.isRight());
    // verify that the user repository updated the user
    ArgumentCaptor<Channel> argument = ArgumentCaptor.forClass(Channel.class);
    verify(mockChannelRepository).update(argument.capture());
    assertThat(argument.getValue().getName(), is("Triple J"));
  }

  @Test public void update_failureWhenFailsValidation() {
    // ARRANGE
    Channel savedChannel = new Channel(
        1L, "FBi Radio", "fbi-radio", "fbi-radio.jpg", "description", null,
        Status.active, ZonedDateTime.now(), ZonedDateTime.now()
    );
    Channel mockChannel = mock(Channel.class);

    when(mockUpdateChannelForm.hasErrors()).thenReturn(true);

    // ACT
    Either<Form<UpdateChannel>, Channel> channelOrError = channelService.update(savedChannel, mockUpdateChannelForm);

    // ASSERT
    // assert right (success value) is not present
    assertFalse(channelOrError.isRight());
    // assert left (error value) is present
    assertTrue(channelOrError.isLeft());
    // verify that the channelRepository never tried to insert the invalid channel
    verify(mockChannelRepository, never()).update(mockChannel);
  }
}
