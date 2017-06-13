package models;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import com.fasterxml.jackson.annotation.JsonBackReference;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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
public class Track extends Model {

  public interface InsertValidators {}
  public interface UpdateValidators {}

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Constraints.Required
  @NotNull
  private String name;

  @ManyToMany
  @JoinTable(name = "track_artist")
  @JsonBackReference(value = "tracks_artists")
  private List<Artist> artists;

  @ManyToMany
  @JoinTable(name = "track_remixer")
  @JsonBackReference(value = "tracks_remixers")
  private List<Artist> remixers;

  private String remixName;

  @ManyToOne
  @JsonBackReference(value = "genre_tracks")
  private Genre genre;

  @ManyToOne
  @JsonBackReference(value = "label_tracks")
  private Label label;

  private LocalDate releaseDate;

  @ManyToMany
  @JsonBackReference(value = "tracklists_tracks")
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

  public List<Artist> getArtists() {
    return artists;
  }

  public List<Artist> getRemixers() {
    return remixers;
  }

  public String getRemixName() {
    return remixName;
  }

  public Genre getGenre() {
    return genre;
  }

  public Label getLabel() {
    return label;
  }

  public LocalDate getReleaseDate() {
    return releaseDate;
  }

  public List<Tracklist> getTracklists() {
    return tracklists;
  }
}
