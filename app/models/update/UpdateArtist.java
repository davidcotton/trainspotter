package models.update;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.Artist;
import play.data.validation.Constraints;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateArtist {

  @Constraints.Required(message = "A name is required.")
  private String name;

  private String image;

  private String description;

  public UpdateArtist(Artist artist) {
    name = artist.getName();
    image = artist.getImage();
    description = artist.getDescription();
  }
}
