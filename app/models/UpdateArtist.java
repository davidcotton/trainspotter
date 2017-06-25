package models;

import lombok.Data;
import lombok.NoArgsConstructor;
import play.data.validation.Constraints;

@Data
@NoArgsConstructor
public class UpdateArtist {

  @Constraints.Required(message = "A name is required.")
  private String name;

  private String slug;

  private String image;

  private String description;

  public UpdateArtist(Artist artist) {
    name = artist.getName();
    slug = artist.getSlug();
    image = artist.getImage();
    description = artist.getImage();
  }
}
