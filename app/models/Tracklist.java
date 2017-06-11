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

  /** Validator group to be called on insert. */
  public interface InsertValidators {}

  /** Validator group to be called on update. */
  public interface UpdateValidators {}

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Constraints.Required
  private String name;

  private LocalDate date;

  @ManyToOne(optional = false)
  @JsonBackReference(value = "user_tracklists")
  private User user;

//  @ManyToMany(mappedBy = "tracklists")
//  @JsonManagedReference(value = "tracklist_tracks")
//  private List<Track> tracks;

//  @ManyToMany
//  @JsonBackReference(value = "artist_tracklists")
//  private List<Artist> artists;

//  @ManyToMany
//  @JsonBackReference(value = "genres_tracklists")
//  private List<Genre> genres;

//  @OneToMany
//  @JsonManagedReference(value = "tracklists_medias")
//  private List<Media> medias;

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
