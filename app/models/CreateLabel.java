package models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import play.data.validation.Constraints;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateLabel {

  @Constraints.Required(message = "A name is required.")
  private String name;

  private String image;

  private String description;
}
