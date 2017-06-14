package models;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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
public class Tracklist extends Model {

  public interface InsertValidators {}
  public interface UpdateValidators {}

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Constraints.Required
  private String name;

  private LocalDate date;

  @JsonBackReference(value = "user_tracklists")
  @ManyToOne(optional = false)
  private User user;

  @JsonManagedReference(value = "tracklists_tracks")
  @ManyToMany(mappedBy = "tracklists")
  private List<Track> tracks;

  @JsonManagedReference(value = "artist_tracklist")
  @ManyToMany
  private List<Artist> artists;

  @JsonManagedReference(value = "genres_tracklists")
  @ManyToMany
  private List<Genre> genres;

  @JsonManagedReference(value = "tracklists_medias")
  @OneToMany
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

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public LocalDate getDate() {
    return date;
  }

  public User getUser() {
    return user;
  }

  public List<Track> getTracks() {
    return tracks;
  }

  public List<Artist> getArtists() {
    return artists;
  }

  public List<Genre> getGenres() {
    return genres;
  }

  public List<Media> getMedias() {
    return medias;
  }
}
