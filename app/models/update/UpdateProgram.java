package models.update;

import lombok.Data;
import lombok.NoArgsConstructor;
import models.Program;
import play.data.validation.Constraints;

@Data
@NoArgsConstructor
public class UpdateProgram {

  @Constraints.Required(message = "A name is required.")
  private String name;
  private String image;
  private String description;

  public UpdateProgram(Program program) {
    name = program.getName();
    image = program.getImage();
    description = program.getDescription();
  }
}
