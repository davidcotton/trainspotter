package models;

import lombok.Data;
import lombok.NoArgsConstructor;
import play.data.validation.Constraints;

@Data
@NoArgsConstructor
public class UpdateChannel {

  @Constraints.Required(message = "A name is required.")
  private String name;

  private String image;

  private String description;

  public UpdateChannel(Channel channel) {
    name = channel.getName();
    image = channel.getImage();
    description = channel.getDescription();
  }
}
