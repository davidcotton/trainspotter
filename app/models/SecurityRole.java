package models;

import be.objectify.deadbolt.java.models.Role;
import com.avaje.ebean.Model;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class SecurityRole extends Model implements Role {

  public static final Finder<Long, SecurityRole> find = new Finder<>(Long.class, SecurityRole.class);

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long id;

  public String name;

  /**
   * Get the name of the role.
   *
   * @return the non-null name of the role
   */
  @Override
  public String getName() {
    return name;
  }

  public static SecurityRole findByName(String name) {
    return find
        .where().eq("name", name)
        .findUnique();
  }
}
