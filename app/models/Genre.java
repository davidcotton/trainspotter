package models;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class Genre extends Model {

  public interface InsertValidators {}
  public interface UpdateValidators {}

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Constraints.Required
  @NotNull
  @Column(unique = true, length = 191)
  private String name;

  @JsonBackReference(value = "genre_tracks")
  @OneToMany(mappedBy = "genre")
  private List<Track> tracks;

  @JsonBackReference(value = "genres_tracklists")
  @ManyToMany(mappedBy = "genres")
  private List<Tracklist> tracklists;

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

  public List<Track> getTracks() {
    return tracks;
  }

  public List<Tracklist> getTracklists() {
    return tracklists;
  }
}
