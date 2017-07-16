package models.update;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.Channel;
import models.Program;
import play.data.validation.Constraints;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProgram {

  @Constraints.Required(message = "A name is required.")
  private String name;
  private String image;
  private String description;
  private Channel channel;

  public UpdateProgram(Program program) {
    name = program.getName();
    image = program.getImage();
    description = program.getDescription();
    channel = program.getChannel();
  }
}
