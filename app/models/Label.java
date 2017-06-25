package models;

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
import play.data.format.Formats;
import play.data.validation.Constraints;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class Label extends Model {

  public interface InsertValidators {}
  public interface UpdateValidators {}

  public enum Status {
    @EnumValue("active") active,
    @EnumValue("deleted") deleted,
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Constraints.Required
  @NotNull
  @Column(unique = true, length = 191)
  private String name;

  @NotNull
  @Constraints.Required
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
    if (image != null) {
      return String.format("images/label/%s", image);
    } else {
      return null;
    }
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
