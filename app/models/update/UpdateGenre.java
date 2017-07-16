package models.update;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.Genre;
import play.data.validation.Constraints;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateGenre {

  @Constraints.Required(message = "A name is required.")
  private String name;

  public UpdateGenre(Genre genre) {
    name = genre.getName();
  }
}
