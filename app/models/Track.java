package models;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.EnumValue;
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
import models.create.CreateTrack;
import models.update.UpdateTrack;
import play.data.format.Formats;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class Track extends Model {

  public enum Status {
    @EnumValue("active") active,
    @EnumValue("deleted") deleted,
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  private String name;

  @JsonBackReference(value = "track_artist")
  @ManyToMany
  @JoinTable(name = "track_artist")
  private List<Artist> artists;

  @JsonBackReference(value = "track_remixer")
  @ManyToMany
  @JoinTable(name = "track_remixer")
  private List<Artist> remixers;

  private String remixName;

  @JsonBackReference(value = "genre_track")
  @ManyToOne
  private Genre genre;

  @JsonBackReference(value = "label_track")
  @ManyToOne
  private Label label;

  private LocalDate releaseDate;

  @JsonBackReference(value = "tracklist_track")
  @ManyToMany
  private List<Tracklist> tracklists;

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

  public Track(CreateTrack createTrack) {
    name = createTrack.getName();
    remixName = createTrack.getRemixName();
    releaseDate = createTrack.getReleaseDate();
    status = Status.active;
  }

  public Track(UpdateTrack updateTrack, Track track) {
    id = track.id;
    name = updateTrack.getName();
    artists = track.artists;
    remixers = track.remixers;
    remixName = updateTrack.getRemixName();
    genre = track.getGenre();
    label = track.getLabel();
    releaseDate = updateTrack.getReleaseDate();
    tracklists = track.tracklists;
    status = track.status;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getFullName() {
    String artistNames = "artist";
    if (remixName != null) {
      return String.format("%s - %s (%s)", artistNames, name, remixName);
    } else {
      return String.format("%s - %s", artistNames, name);
    }
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
