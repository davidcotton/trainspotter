package models;

import static utilities.ImageHelper.imagify;
import static utilities.SlugHelper.slugify;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.EnumValue;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.time.ZonedDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import models.create.CreateChannel;
import models.update.UpdateChannel;
import utilities.ImageHelper;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class Channel extends Model {

  private static final String NICHE = "channel";

  public enum Status {
    @EnumValue("active") active,
    @EnumValue("deleted") deleted,
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Column(unique = true, length = 191)
  private String name;

  @NotNull
  @Column(unique = true, length = 191)
  private String slug;

  private String image;

  @Column(columnDefinition = "text")
  private String description;

  @JsonManagedReference(value = "channel_program")
  @OneToMany(mappedBy = "channel", cascade = CascadeType.PERSIST)
  private List<Program> programs;

  @NotNull
  private Status status;

  @CreatedTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  private ZonedDateTime created;

  @UpdatedTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  private ZonedDateTime updated;

  public Channel(CreateChannel createChannel) {
    name = createChannel.getName();
    slug = slugify(createChannel.getName());
    image = imagify(createChannel.getImage(), NICHE);
    description = createChannel.getDescription();
    status = Status.active;
  }

  public Channel(UpdateChannel updateChannel, Channel channel) {
    id = channel.id;
    name = updateChannel.getName();
    slug = channel.slug;
    image = imagify(updateChannel.getImage(), NICHE);
    description = updateChannel.getDescription();
    programs = channel.programs;
    status = channel.status;
    created = channel.created;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getSlug() {
    return slug;
  }

  public String getImage() {
    return image;
  }

  public String getImageLink() {
    return ImageHelper.getLink(image, NICHE);
  }

  public String getDescription() {
    return description;
  }

  public List<Program> getPrograms() {
    return programs;
  }
}
