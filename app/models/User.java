package models;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.EnumValue;
import com.avaje.ebean.annotation.UpdatedTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.ZonedDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import play.data.format.Formats;
import play.data.validation.Constraints;
import validators.CustomConstraints;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class User extends Model {

  public enum Status {
    @EnumValue("inactive")inactive,
    @EnumValue("unverified")unverified,
    @EnumValue("active")active,
    @EnumValue("deleted")deleted,
    @EnumValue("banned")banned,
  }

  /** Validator group to be called on insert. */
  public interface InsertValidators {}

  /** Validator group to be called on update. */
  public interface UpdateValidators {}

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Constraints.Required(
      message = "An email address is required.",
      groups = {InsertValidators.class, UpdateValidators.class}
  )
  @Constraints.Email(
      message = "This is not a valid email address.",
      groups = {InsertValidators.class, UpdateValidators.class}
  )
  @CustomConstraints.UniqueEmail(groups = {InsertValidators.class})
  @NotNull
  @Column(unique = true, length = 191)
  private String email;

  @Constraints.Required(
      message = "A display name is required.",
      groups = {InsertValidators.class, UpdateValidators.class}
  )
  @NotNull
  @Column(unique = true, length = 191)
  private String displayName;

  @NotNull
  @Enumerated
  private Status status;

  @OneToOne
  private Password password;

//  @OneToMany(mappedBy = "user")
//  @JsonManagedReference(value = "user_tracklists")
//  private List<Tracklist> tracklists;

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

//  public User(CreateUser createUser) {
//    this.email = createUser.getEmail();
//    this.displayName = createUser.getName();
//    this.setStatus(Status.unverified);
//  }
}
