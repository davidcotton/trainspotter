package models;

import lombok.Data;
import play.data.validation.Constraints;

@Data
public class LoginUser {

  public interface Validators {}

  @Constraints.Required(message = "An email address is required.")
  private String email;

  @Constraints.Required(message = "A password is required.")
  private String password;
}
