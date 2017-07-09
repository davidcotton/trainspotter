package models;

import static utilities.ImageHelper.imagify;
import static utilities.SlugHelper.slugify;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.EnumValue;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.time.ZonedDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import models.create.CreateProgram;
import models.update.UpdateProgram;
import play.data.format.Formats;
import utilities.ImageHelper;
import utilities.TextHelper;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class Program extends Model {

  private static final String NICHE = "program";

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

  @JsonBackReference(value = "channel_program")
  @ManyToOne
  private Channel channel;

  @JsonManagedReference(value = "program_host")
  @ManyToMany(mappedBy = "programs", cascade = CascadeType.PERSIST)
  private List<Artist> hosts;

  @NotNull
  private Status status;

  @CreatedTimestamp
  @Formats.DateTime(pattern = "yyyy-MM-dd HH:mm:ss")
  @Temporal(TemporalType.TIMESTAMP)
  @Column(columnDefinition = "datetime")
  private ZonedDateTime created;

  @UpdatedTimestamp
  @Formats.DateTime(pattern = "yyyy-MM-dd HH:mm:ss")
  @Temporal(TemporalType.TIMESTAMP)
  @Column(columnDefinition = "datetime")
  private ZonedDateTime updated;

  public Program(CreateProgram createProgram) {
    name = createProgram.getName();
    slug = slugify(createProgram.getName());
    image = imagify(createProgram.getImage(), NICHE);
    description = createProgram.getDescription();
    channel = createProgram.getChannel();
    status = Status.active;
  }

  public Program(UpdateProgram updateProgram, Program program) {
    id = program.id;
    name = updateProgram.getName();
    slug = program.slug;
    image = imagify(updateProgram.getImage(), NICHE);
    description = updateProgram.getDescription();
    channel = updateProgram.getChannel();
    hosts = program.getHosts();
    status = program.status;
    created = program.created;
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

  public String getDescriptionExcerpt() {
    return TextHelper.truncate(description, 200);
  }

  public Channel getChannel() {
    return channel;
  }

  public List<Artist> getHosts() {
    return hosts;
  }

  public void addHost(Artist artist) {
    hosts.add(artist);
  }
}
