package models;

import lombok.Data;
import play.data.validation.Constraints;

@Data
public class AddTrack {

  @Constraints.Required(message = "A name is required.")
  private String track;
}
