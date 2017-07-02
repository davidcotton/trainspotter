package models;

import be.objectify.deadbolt.java.models.Permission;
import com.avaje.ebean.Model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class UserPermission extends Model implements Permission {

  public static final Finder<Long, UserPermission> find = new Finder<>(Long.class, UserPermission.class);

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long id;

  @Column(name = "permission_value")
  public String value;

  /**
   * Get the value of the permission.
   *
   * @return a non-null String representation of the permission
   */
  @Override
  public String getValue() {
    return value;
  }

  public static UserPermission findByValue(String value) {
    return find
        .where().eq("value", value)
        .findUnique();
  }
}
