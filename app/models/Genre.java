package models;

import static utilities.SlugHelper.slugify;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.EnumValue;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
import models.create.CreateGenre;
import models.update.UpdateGenre;
import play.data.validation.Constraints;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class Genre extends Model {

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

  @JsonBackReference(value = "genre_track")
  @OneToMany(mappedBy = "genre", cascade = CascadeType.PERSIST)
  private List<Track> tracks;

  @JsonBackReference(value = "genre_tracklist")
  @ManyToMany(mappedBy = "genres", cascade = CascadeType.PERSIST)
  private List<Tracklist> tracklists;

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

  public Genre(CreateGenre createGenre) {
    name = createGenre.getName();
    slug = slugify(createGenre.getName());
    status = Status.active;
  }

  public Genre(UpdateGenre updateGenre, Genre genre) {
    id = genre.id;
    name = updateGenre.getName();
    slug = genre.slug;
    tracks = genre.tracks;
    tracklists = genre.tracklists;
    status = genre.status;
    created = genre.created;
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

  public List<Track> getTracks() {
    return tracks;
  }

  public List<Tracklist> getTracklists() {
    return tracklists;
  }
}
