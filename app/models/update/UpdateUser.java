package models.update;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.User;
import play.data.validation.Constraints;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUser {

  @Constraints.Required(message = "An email address is required.")
  @Constraints.Email(message = "This is not a valid email address.")
  private String email;

  @Constraints.Required(message = "A username is required.")
  private String username;

  public UpdateUser(User user) {
    email = user.getEmail();
    username = user.getUsername();
  }
}
