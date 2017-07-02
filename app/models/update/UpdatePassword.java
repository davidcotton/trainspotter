package models.update;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import play.data.validation.Constraints;
import validators.CustomConstraints;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePassword {

  @Constraints.Required(message = "Your current password is required.")
  private String currentPassword;

  @Constraints.Required(message = "Your new password is required.")
  @CustomConstraints.Password
  private String newPassword;

  @Constraints.Required(message = "Your need to confirm your password.")
  private String confirmNewPassword;
}
