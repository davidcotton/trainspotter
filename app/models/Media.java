package models;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.EnumValue;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import com.fasterxml.jackson.annotation.JsonBackReference;
import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import play.data.validation.Constraints;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class Media extends Model {

  public enum Status {
    @EnumValue("pending") pending,
    @EnumValue("active") active,
    @EnumValue("deleted") deleted,
  }

  public enum Type {
    @EnumValue("soundcloud") soundcloud,
    @EnumValue("mixcloud") mixcloud,
    @EnumValue("youtube") youtube,
    @EnumValue("twitter") twitter,
    @EnumValue("facebook") facebook,
    @EnumValue("website") website,
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Constraints.Required
  @Column(unique = true, length = 191)
  private String url;

  @NotNull
  private Type type;

  @JsonBackReference(value = "tracklist_media")
  @ManyToOne
  private Tracklist tracklist;

  @JsonBackReference(value = "artist_media")
  @ManyToOne
  private Artist artist;

  @JsonBackReference(value = "label_media")
  @ManyToOne
  private Label label;

  @JsonBackReference(value = "user_media")
  @ManyToOne
  private User user;

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

  public Long getId() {
    return id;
  }

  public String getType() {
    return type.name();
  }

  public String getUrl() {
    return url;
  }

  public Tracklist getTracklist() {
    return tracklist;
  }

  public Artist getArtist() {
    return artist;
  }

  public Label getLabel() {
    return label;
  }
}
