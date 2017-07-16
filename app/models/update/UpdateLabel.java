package models.update;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.Label;
import play.data.validation.Constraints;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateLabel {

  @Constraints.Required(message = "A name is required.")
  private String name;

  private String image;

  private String description;

  public UpdateLabel(Label label) {
    name = label.getName();
    image = label.getImage();
    description = label.getDescription();
  }
}
