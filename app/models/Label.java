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
import models.create.CreateLabel;
import models.update.UpdateLabel;
import utilities.ImageHelper;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class Label extends Model {

  private static final String NICHE = "label";

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

  @JsonManagedReference(value = "label_track")
  @OneToMany(mappedBy = "label", cascade = CascadeType.PERSIST)
  private List<Track> tracks;

  @JsonManagedReference(value = "label_media")
  @OneToMany(mappedBy = "label", cascade = CascadeType.PERSIST)
  private List<Media> medias;

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

  public Label(CreateLabel createLabel) {
    name = createLabel.getName();
    slug = slugify(createLabel.getName());
    image = imagify(createLabel.getImage(), NICHE);
    description = createLabel.getDescription();
    status = Status.active;
  }

  public Label(UpdateLabel updateLabel, Label label) {
    id = label.id;
    name = updateLabel.getName();
    slug = label.slug;
    image = imagify(updateLabel.getImage(), NICHE);
    description = updateLabel.getDescription();
    tracks = label.tracks;
    medias = label.medias;
    status = label.status;
    created = label.created;
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

  public List<Track> getTracks() {
    return tracks;
  }

  public List<Media> getMedias() {
    return medias;
  }
}
