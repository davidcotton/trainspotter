package models.create;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import play.data.validation.Constraints;
import validators.CustomConstraints;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUser {

  @Constraints.Required(message = "An email address is required.")
  @Constraints.Email(message = "This is not a valid email address.")
  @CustomConstraints.UniqueEmail
  private String email;

  @Constraints.Required(message = "A display name is required.")
  private String displayName;

  @Constraints.Required(message = "A password is required.")
  @CustomConstraints.Password
  private String password;
}
