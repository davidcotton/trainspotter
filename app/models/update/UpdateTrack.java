package models.update;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.Artist;
import models.Genre;
import models.Label;
import models.Track;
import play.data.validation.Constraints;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTrack {

  @Constraints.Required(message = "A name is required.")
  private String name;
  private List<Artist> artists;
  private List<Artist> remixers;
  private String remixName;
  private Label label;
  private Genre genre;
  private LocalDate releaseDate;

  public UpdateTrack(Track track) {
    name = track.getName();
    artists = track.getArtists();
    remixers = track.getRemixers();
    remixName = track.getRemixName();
    genre = track.getGenre();
    label = track.getLabel();
    releaseDate = track.getReleaseDate();
  }
}
