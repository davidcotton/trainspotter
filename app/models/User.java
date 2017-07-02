package models;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.EnumValue;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import org.mindrot.jbcrypt.BCrypt;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import be.objectify.deadbolt.java.models.Permission;
import be.objectify.deadbolt.java.models.Role;
import be.objectify.deadbolt.java.models.Subject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import models.create.CreateUser;
import models.update.UpdatePassword;
import models.update.UpdateUser;
import play.data.format.Formats;

import static utilities.SlugHelper.slugify;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class User extends Model implements Subject {

  private static final int bcryptWorkFactor = 16;

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
  private String username;

  @NotNull
  @Column(unique = true, length = 191)
  private String slug;

  @NotNull
  @Enumerated
  private Status status;

  private long karma;

  private long tracklistsCreated;

  private long tracksIdentified;

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

  @ManyToMany
  public List<SecurityRole> roles;

  @ManyToMany
  public List<UserPermission> permissions;

  @JsonManagedReference(value = "user_tracklist")
  @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
  private List<Tracklist> tracklists;

  @JsonManagedReference(value = "user_media")
  @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
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

  public User(CreateUser createUser) {
    email = createUser.getEmail();
    username = createUser.getUsername();
    slug = slugify(createUser.getUsername());
    karma = 0;
    tracklistsCreated = 0;
    tracksIdentified = 0;
    salt = generateSalt();
    hash = hashPassword(createUser.getPassword(), salt);
    status = Status.unverified;
  }

  public User(UpdateUser updateUser, User savedUser) {
    id = savedUser.id;
    email = updateUser.getEmail();
    username = updateUser.getUsername();
    slug = slugify(updateUser.getUsername());
    karma = savedUser.karma;
    tracklistsCreated = savedUser.tracklistsCreated;
    tracksIdentified = savedUser.tracksIdentified;
    status = savedUser.status;
    hash = savedUser.hash;
    salt = savedUser.salt;
    tracklists = savedUser.tracklists;
    medias = savedUser.medias;
    created = savedUser.created;
  }

  public User(UpdatePassword updatePassword, User savedUser) {
    id = savedUser.id;
    email = savedUser.email;
    username = savedUser.username;
    slug = savedUser.slug;
    status = savedUser.status;
    karma = savedUser.karma;
    tracklistsCreated = savedUser.tracklistsCreated;
    tracksIdentified = savedUser.tracksIdentified;
    salt = generateSalt();
    hash = hashPassword(updatePassword.getNewPassword(), salt);
    tracklists = savedUser.tracklists;
    medias = savedUser.medias;
    created = savedUser.created;
  }

  /**
   * Check that a plaintext password matches the hashed password.
   *
   * @param plaintext The plaintext password to verify.
   * @return true if the passwords match, false otherwise.
   */
  public boolean isAuthorised(String plaintext) {
    return BCrypt.checkpw(plaintext, hash);
  }

  /**
   * Generate a salt.
   *
   * @return The generated salt.
   */
  private String generateSalt() {
    return BCrypt.gensalt(bcryptWorkFactor);
  }

  /**
   * Hash a password with BCrypt.
   *
   * @param plaintext The plaintext password to hash.
   * @param salt      The salt to use during hashing.
   * @return The hashed password.
   */
  private String hashPassword(String plaintext, String salt) {
    return BCrypt.hashpw(plaintext, salt);
  }

  public Long getId() {
    return id;
  }

  public String getEmail() {
    return email;
  }

  public String getUsername() {
    return username;
  }

  public String getSlug() {
    return slug;
  }

  public long getKarma() {
    return karma;
  }

  public long getTracklistsCreated() {
    return tracklistsCreated;
  }

  public long getTracksIdentified() {
    return tracksIdentified;
  }

  public Status getStatus() {
    return status;
  }

  /**
   * Get all {@link Role}s held by this subject.  Ordering is not important.
   *
   * @return a non-null list of roles
   */
  @Override
  public List<? extends Role> getRoles() {
    return roles;
  }

  /**
   * Get all {@link Permission}s held by this subject.  Ordering is not important.
   *
   * @return a non-null list of permissions
   */
  @Override
  public List<? extends Permission> getPermissions() {
    return permissions;
  }

  /**
   * Gets a unique identifier for the subject, such as a user name.
   * This is never used by Deadbolt itself, and is present to provide an easy way of
   * getting a useful piece of user information in, for example,
   * dynamic checks without the need to cast the Subject.
   *
   * @return an identifier, such as a user name or UUID.  May be null.
   */
  @Override
  public String getIdentifier() {
    return username;
  }

  public List<Tracklist> getTracklists() {
    return tracklists;
  }

  public String getRegistered() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
    return created.format(formatter);
  }
}
