package models;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import play.data.validation.Constraints;

@Entity
@Data
@EqualsAndHashCode
@AllArgsConstructor
public class Tracklist extends Model {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Constraints.Required
  private String name;

  @ManyToOne
  private List<Track> tracks;

  private LocalDate date;

  @CreatedTimestamp
  private ZonedDateTime created;
}
