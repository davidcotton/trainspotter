package models.update;

import play.data.validation.Constraints;
import validators.CustomConstraints;

public class UpdatePassword {

  @Constraints.Required(message = "A password is required.")
  @CustomConstraints.Password
  private String password;
}
