package models;

import com.avaje.ebean.annotation.DocStore;
import java.util.List;
import java.util.Map;
import javax.persistence.Entity;

import lombok.Data;

@DocStore
@Entity
@Data
public class RestaurantDoc {

  public String name;
  public String description;
  public Map<String, String> address;
  public List<String> location;
  public List<String> tags;
  public float rating;
}
