package models.create;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.Artist;
import play.data.validation.Constraints;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTracklist {

  @Constraints.Required(message = "A name is required.")
  private String name;

  @Constraints.Required(message = "Performed date is required.")
  private LocalDate performed;

  private String image;

  private List<Artist> artists;
}
