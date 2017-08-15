package models.update;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.Artist;
import models.Tracklist;
import play.data.validation.Constraints;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTracklist {

  @Constraints.Required(message = "A name is required.")
  private String name;

  @Constraints.Required(message = "Performed date is required.")
  private LocalDate performed;

  private String image;

  private List<Artist> artists;

  public UpdateTracklist(Tracklist tracklist) {
    name = tracklist.getName();
    performed = tracklist.getPerformed();
    image = tracklist.getImage();
    artists = tracklist.getArtists();
  }
}
