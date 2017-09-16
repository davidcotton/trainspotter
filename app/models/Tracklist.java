package models;

import static utilities.ImageHelper.imagify;
import static utilities.SlugHelper.slugify;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.EnumValue;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.time.Duration;
import java.time.LocalDate;
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
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import models.create.CreateTracklist;
import models.update.UpdateTracklist;
import utilities.ImageHelper;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class Tracklist extends Model {

  private static final String NICHE = "tracklist";

  public enum Status {
    @EnumValue("active") active,
    @EnumValue("deleted") deleted,
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  private String name;

  @NotNull
  @Column(unique = true, length = 191)
  private String slug;

  @NotNull
  private LocalDate performed;

  private String image;

  @JsonBackReference(value = "user_tracklist")
  @ManyToOne(optional = false)
  private User user;

  @JsonManagedReference(value = "tracklist_usertrack")
  @OneToMany(cascade = CascadeType.PERSIST)
  @OrderBy("position ASC")
  private List<UserTrack> userTracks;

  @JsonManagedReference(value = "artist_tracklist")
  @ManyToMany
  private List<Artist> artists;

  @JsonManagedReference(value = "genre_tracklist")
  @ManyToMany
  private List<Genre> genres;

  @JsonManagedReference(value = "tracklist_media")
  @OneToMany
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

  public Tracklist(CreateTracklist createTracklist) {
    name = createTracklist.getName();
    slug = slugify(createTracklist.getName());
    performed = createTracklist.getPerformed();
    image = imagify(createTracklist.getImage(), NICHE);
    artists = createTracklist.getArtists();
    status = Status.active;
  }

  public Tracklist(UpdateTracklist updateTracklist, Tracklist tracklist) {
    id = tracklist.id;
    name = updateTracklist.getName();
    slug = tracklist.slug;
    performed = updateTracklist.getPerformed();
    image = imagify(updateTracklist.getImage(), NICHE);
    user = tracklist.user;
    userTracks = tracklist.userTracks;
    artists = updateTracklist.getArtists();
    genres = tracklist.genres;
    medias = tracklist.medias;
    status = tracklist.status;
    created = tracklist.created;
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

  public LocalDate getPerformed() {
    return performed;
  }

  /**
   * Get the number of days ago the set was performed.
   */
  public long getPerformedDaysAgo() {
    return Duration.between(performed.atStartOfDay(), LocalDate.now().atStartOfDay()).toDays();
  }

  public String getImage() {
    return image;
  }

  /**
   * Get a formatted image URL.
   */
  public String getImageLink() {
    return ImageHelper.getLink(image, NICHE);
  }

  public User getUser() {
    return user;
  }

  public List<UserTrack> getUserTracks() {
    return userTracks;
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
