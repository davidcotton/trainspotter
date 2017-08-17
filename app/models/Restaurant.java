package models;

import com.github.cleverage.elasticsearch.Indexable;
import java.util.List;
import java.util.Map;

public class Restaurant implements Indexable {

  public String name;
  public String description;
  public Map<String, String> address;
  public List<String> location;
  public List<String> tags;
  public float rating;

  @Override
  public Map toIndex() {

    return null;
  }

  @Override
  public Indexable fromIndex(Map map) {
    if (map == null) {
      return this;
    }

    return null;
  }
}
