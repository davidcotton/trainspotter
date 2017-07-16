package models.update;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.Channel;
import play.data.validation.Constraints;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
