package models;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;

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
public class Track extends Model {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Constraints.Required
  private String name;

  @ManyToOne
  private List<Artist> artists;

  @CreatedTimestamp
  private ZonedDateTime created;
}
