package services;

import static java.util.Objects.requireNonNull;

import com.avaje.ebean.PagedList;
import io.atlassian.fugue.Either;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import models.Channel;
import models.Channel.Status;
import models.create.CreateChannel;
import models.update.UpdateChannel;
import play.data.Form;
import repositories.ChannelRepository;

public class ChannelService {

  private final ChannelRepository channelRepository;

  @Inject
  public ChannelService(ChannelRepository channelRepository) {
    this.channelRepository = requireNonNull(channelRepository);
  }

  /**
   * Fetch all Channels.
   *
   * @return A collection of all Channels.
   */
  public List<Channel> fetchAll() {
    return channelRepository.findAll();
  }

  /**
   * Fetch a paginated collection of Channels.
   *
   * @param page The paginator page number.
   * @return A paginated list of Channels, ordered by name.
   */
  public PagedList<Channel> fetchAllPaged(int page) {
    return channelRepository.findAllPaged(page);
  }

  /**
   * Find a Channel by its ID.
   *
   * @param id The ID to search for.
   * @return An optional Channel if found.
   */
  public Optional<Channel> findById(long id) {
    return channelRepository.findById(id);
  }

  /**
   * Search for a Channel by its slug.
   *
   * @param slug The slug of the Channel to search for.
   * @return An optional Channel if found.
   */
  public Optional<Channel> findBySlug(String slug) {
    return channelRepository.findBySlug(slug);
  }

  /**
   * Insert a new Channel.
   *
   * @param channelForm The submitted Channel data form.
   * @return Either the inserted Channel or the form with errors.
   */
  public Either<Form<CreateChannel>, Channel> insert(Form<CreateChannel> channelForm) {
    if (channelForm.hasErrors()) {
      return Either.left(channelForm);
    }

    Channel channel = new Channel(channelForm.get());
    // save to DB
    channelRepository.insert(channel);

    // return saved channel
    return Either.right(channel);
  }

  /**
   * Update an Channel.
   *
   * @param savedChannel The existing Channel.
   * @param channelForm  The new Channel data form.
   * @return Either the updated Channel or the form with errors.
   */
  public Either<Form<UpdateChannel>, Channel> update(Channel savedChannel, Form<UpdateChannel> channelForm) {
    if (channelForm.hasErrors()) {
      return Either.left(channelForm);
    }

    UpdateChannel updateChannel = channelForm.get();
    Channel newChannel = new Channel(updateChannel, savedChannel);

    // save to DB
    channelRepository.update(newChannel);

    // return saved channel
    return Either.right(newChannel);
  }

  /**
   * Delete a Channel.
   *
   * @param channel The Channel to delete.
   */
  public void delete(Channel channel) {
    channel.setStatus(Status.deleted);
    channelRepository.update(channel);
  }
}
