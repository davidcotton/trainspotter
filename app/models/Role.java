package models;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
public class Role extends Model implements be.objectify.deadbolt.java.models.Role {

  public static final Finder<Long, Role> find = new Finder<>(Long.class, Role.class);
  public static final String SUPER_ADMIN = "super_admin";
  public static final String ADMIN = "admin";
  public static final String EDITOR = "editor";
  public static final String CONTRIBUTOR = "contributor";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long id;

  @NotNull
  @Column(unique = true, length = 191)
  public String name;

  @CreatedTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  private ZonedDateTime created;

  @UpdatedTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  private ZonedDateTime updated;

  /**
   * Get the name of the role.
   *
   * @return the non-null name of the role
   */
  @Override
  public String getName() {
    return name;
  }

  public static Role findByName(String name) {
    return find
        .where().eq("name", name)
        .findUnique();
  }
}
