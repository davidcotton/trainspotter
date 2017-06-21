package models;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
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

  public interface InsertValidators {}
  public interface UpdateValidators {}

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Constraints.Required
  @Column(unique = true, length = 191)
  private String name;

  @NotNull
  @Constraints.Required
  @Column(unique = true, length = 191)
  private String slug;

  private String image;

  @Column(columnDefinition = "text")
  private String description;

  @JsonManagedReference(value = "track_artist")
  @ManyToMany(mappedBy = "artists", cascade = CascadeType.PERSIST)
  private List<Track> tracks;

  @JsonManagedReference(value = "track_remixer")
  @ManyToMany(mappedBy = "remixers", cascade = CascadeType.PERSIST)
  private List<Track> remixes;

  @JsonBackReference(value = "artist_tracklist")
  @ManyToMany(mappedBy = "artists", cascade = CascadeType.PERSIST)
  private List<Tracklist> tracklists;

  @JsonManagedReference(value = "artist_media")
  @OneToMany(mappedBy = "artist", cascade = CascadeType.PERSIST)
  private List<Media> medias;

  @JsonBackReference(value = "program_host")
  @ManyToMany
  private List<Program> programs;

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
    if (image == null) {
      return null;
    } else {
      return String.format("images/artist/%s", image);
    }
  }

  public String getDescription() {
    return description;
  }

  public List<Track> getTracks() {
    return tracks;
  }

  public List<Track> getRemixes() {
    return remixes;
  }

  public List<Tracklist> getTracklists() {
    return tracklists;
  }

  public List<Media> getMedias() {
    return medias;
  }

  public List<Program> getPrograms() {
    return programs;
  }
}
