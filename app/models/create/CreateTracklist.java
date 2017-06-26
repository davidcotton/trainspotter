package models.create;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import play.data.validation.Constraints;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTracklist {

  @Constraints.Required(message = "A name is required.")
  private String name;
  private LocalDate performed;
  private String image;
}
