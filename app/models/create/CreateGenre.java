package models.create;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import play.data.validation.Constraints;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateGenre {

  @Constraints.Required(message = "A name is required.")
  private String name;
}
