package models.update;

import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.Label;
import models.Track;
import play.data.validation.Constraints;

@Data
@NoArgsConstructor
public class UpdateTrack {

  @Constraints.Required(message = "A name is required.")
  private String name;
  private String remixName;
  private Label label;
  private LocalDate releaseDate;

  public UpdateTrack(Track track) {
    name = track.getName();
    remixName = track.getRemixName();
    label = track.getLabel();
    releaseDate = track.getReleaseDate();
  }
}
