package services;

import static java.util.Objects.requireNonNull;
import static play.libs.Json.toJson;

import io.atlassian.fugue.Either;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.inject.Inject;
import models.Channel;
import play.data.Form;
import play.data.FormFactory;
import play.data.validation.ValidationError;
import repositories.ChannelRepository;

public class ChannelService {

  private final ChannelRepository channelRepository;
  private final FormFactory formFactory;

  @Inject
  public ChannelService(ChannelRepository channelRepository, FormFactory formFactory) {
    this.channelRepository = requireNonNull(channelRepository);
    this.formFactory = requireNonNull(formFactory);
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
   * Find a Channel by its ID.
   *
   * @param id The ID to search for.
   * @return An optional Channel if found.
   */
  public Optional<Channel> findById(long id) {
    return channelRepository.findById(id);
  }

  /**
   * Insert a new Channel.
   *
   * @param channel The Channel to insert.
   * @return Either the inserted Channel or validation errors.
   */
  public Either<Map<String, List<ValidationError>>, Channel> insert(Channel channel) {
    // validate new channel
    Form<Channel> channelForm = formFactory
        .form(Channel.class, Channel.InsertValidators.class)
        .bind(toJson(channel));
    if (channelForm.hasErrors()) {
      // return validation errors
      return Either.left(channelForm.errors());
    }

    // save to DB
    channelRepository.insert(channel);

    // return saved channel
    return Either.right(channel);
  }

  /**
   * Update a Channel.
   *
   * @param savedChannel  The existing Channel data.
   * @param newChannel    The new Channel data.
   * @return Either the update Channel or validation errors.
   */
  public Either<Map<String, List<ValidationError>>, Channel> update(Channel savedChannel, Channel newChannel) {
    // copy over read only fields
    newChannel.setId(savedChannel.getId());
    newChannel.setCreated(savedChannel.getCreated());

    // validate the changes
    Form<Channel> channelForm = formFactory
        .form(Channel.class, Channel.UpdateValidators.class)
        .bind(toJson(newChannel));
    if (channelForm.hasErrors()) {
      // return validation errors
      return Either.left(channelForm.errors());
    }

    // save to DB
    channelRepository.update(newChannel);

    // return saved channel
    return Either.right(newChannel);
  }
}
