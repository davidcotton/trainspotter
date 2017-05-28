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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import lombok.Getter;
import lombok.Setter;
import play.data.format.Formats;

import static services.AuthenticationService.generateSalt;
import static services.AuthenticationService.hashPassword;

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

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Column(unique = true, length = 191)
  private String email;

  @NotNull
  @Column(unique = true, length = 191)
  private String displayName;

  @NotNull
  @Enumerated
  private Status status;

  @Getter(onMethod = @__(@JsonIgnore))
  @Setter
  @NotNull
  @Column(columnDefinition = "char(60)")
  private String hash;

  @Getter(onMethod = @__(@JsonIgnore))
  @Setter
  @NotNull
  @Column(columnDefinition = "char(29)")
  private String salt;

  @OneToMany(mappedBy = "user")
  @JsonManagedReference
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

  public User(CreateUser createUser) {
    this.email = createUser.getEmail();
    this.displayName = createUser.getName();
    this.salt = generateSalt();
    this.hash = hashPassword(createUser.getPassword(), this.salt);
    this.setStatus(Status.unverified);
  }
}
