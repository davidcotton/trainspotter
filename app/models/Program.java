package models;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import play.data.format.Formats;
import play.data.validation.Constraints;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class Program extends Model {

  public interface InsertValidators {}
  public interface UpdateValidators {}

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Constraints.Required
  @Column(unique = true, length = 191)
  private String name;

  private String image;

  @Column(columnDefinition = "text")
  private String description;

  @JsonBackReference(value = "channel_programs")
  @ManyToOne
  private Channel channel;

  @JsonManagedReference(value = "program_host")
  @ManyToMany(mappedBy = "programs")
  private List<Artist> hosts;

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

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getImage() {
    return image;
  }

  public String getDescription() {
    return description;
  }

  public Channel getChannel() {
    return channel;
  }

  public List<Artist> getHosts() {
    return hosts;
  }

  public void addHost(Artist artist) {
//    if (hosts == null) {
//      hosts = new ArrayList<>();
//    }
//    artist.setPrograms(this);

    List<Program> programs = artist.getPrograms();
    programs.add(this);
    artist.setPrograms(programs);

    hosts.add(artist);

  }
}
