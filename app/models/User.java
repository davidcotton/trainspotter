package models;

import static services.AuthenticationService.generateSalt;
import static services.AuthenticationService.hashPassword;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.EnumValue;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.time.ZonedDateTime;
import java.util.List;
import javax.persistence.CascadeType;
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

  @JsonManagedReference(value = "user_tracklist")
  @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
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
    email = createUser.getEmail();
    displayName = createUser.getDisplayName();
    salt = generateSalt();
    hash = hashPassword(createUser.getPassword(), salt);
    status = Status.unverified;
  }

  public User(UpdateUser updateUser) {
    email = updateUser.getEmail();
    displayName = updateUser.getDisplayName();
    status = Status.unverified;
  }

  public Long getId() {
    return id;
  }

  public String getEmail() {
    return email;
  }

  public String getDisplayName() {
    return displayName;
  }

  public Status getStatus() {
    return status;
  }

  public List<Tracklist> getTracklists() {
    return tracklists;
  }
}
