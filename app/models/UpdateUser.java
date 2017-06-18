package models;

import lombok.Data;
import play.data.validation.Constraints;
import validators.CustomConstraints;

@Data
public class UpdateUser {

  @Constraints.Required(message = "An email address is required.")
  @Constraints.Email(message = "This is not a valid email address.")
  @CustomConstraints.UniqueEmail
  private String email;

  @Constraints.Required(message = "A display name is required.")
  private String displayName;

  public UpdateUser(User user) {
    email = user.getEmail();
    displayName = user.getDisplayName();
  }
}
