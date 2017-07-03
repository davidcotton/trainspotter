package models.update;

import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.Tracklist;
import play.data.validation.Constraints;

@Data
@NoArgsConstructor
public class UpdateTracklist {

  @Constraints.Required(message = "A name is required.")
  private String name;
  private LocalDate performed;
  private String image;

  public UpdateTracklist(Tracklist tracklist) {
    name = tracklist.getName();
    performed = tracklist.getPerformed();
    image = tracklist.getImage();
  }
}