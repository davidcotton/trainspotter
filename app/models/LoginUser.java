package models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import play.data.validation.Constraints;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUser {

  public interface Validators {}

  @Constraints.Required(message = "An email address is required.")
  private String email;

  @Constraints.Required(message = "A password is required.")
  private String password;
}
