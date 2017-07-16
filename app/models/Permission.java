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
public class Permission extends Model implements be.objectify.deadbolt.java.models.Permission {

  public static final Finder<Long, Permission> find = new Finder<>(Long.class, Permission.class);

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long id;

  @NotNull
  @Column(name = "permission_value")
  public String value;

  @CreatedTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  private ZonedDateTime created;

  @UpdatedTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  private ZonedDateTime updated;

  /**
   * Get the value of the permission.
   *
   * @return a non-null String representation of the permission
   */
  @Override
  public String getValue() {
    return value;
  }

  public static Permission findByValue(String value) {
    return find
        .where().eq("value", value)
        .findUnique();
  }
}
