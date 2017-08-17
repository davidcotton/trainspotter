package models;

import com.github.cleverage.elasticsearch.Index;
import com.github.cleverage.elasticsearch.Indexable;
import com.github.cleverage.elasticsearch.annotations.IndexType;
import java.util.HashMap;
import java.util.Map;

@IndexType(name = "tracks")
public class TrackIndex extends Index {

  public String name;

  // Find method static for request
  public static Finder<TrackIndex> find = new Finder<>(TrackIndex.class);

  @Override
  public Map toIndex() {
    Map<String, Object> map = new HashMap<>();
    map.put("name", name);
    return map;
  }

  @Override
  public Indexable fromIndex(Map map) {
    this.name = (String) map.get("name");
    return this;
  }
}
