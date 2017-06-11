package models;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.time.ZonedDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
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
public class Artist extends Model {

  /** Validator group to be called on insert. */
  public interface InsertValidators {}

  /** Validator group to be called on update. */
  public interface UpdateValidators {}

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Constraints.Required
  @Column(unique = true, length = 191)
  private String name;

  private String image;

  @ManyToMany(mappedBy = "artists")
  @JsonManagedReference(value = "track_artists")
  private List<Track> tracks;

  @ManyToMany(mappedBy = "artists")
  @JsonManagedReference(value = "track_remixers")
  private List<Track> remixes;

  @ManyToMany(mappedBy = "artists")
  @JsonBackReference(value = "artist_tracklists")
  private List<Tracklist> tracklists;

  @OneToMany
  @JsonManagedReference(value = "artist_medias")
  private List<Media> medias;

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
}
