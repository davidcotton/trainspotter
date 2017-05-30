package models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import play.data.validation.Constraints;
import validators.CustomConstraints;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUser {

  /** Validator group to be called on insert. */
  public interface InsertValidators {}

  /** Validator group to be called on update. */
  public interface UpdateValidators {}

  @Constraints.Required(
      message = "An email address is required.",
      groups = {InsertValidators.class, UpdateValidators.class}
  )
  @Constraints.Email(
      message = "This is not a valid email address.",
      groups = {InsertValidators.class, UpdateValidators.class}
  )
  @CustomConstraints.UniqueEmail(groups = {InsertValidators.class})
  private String email;

  @Constraints.Required(
      message = "A display name is required.",
      groups = {InsertValidators.class, UpdateValidators.class}
  )
  private String displayName;

  @Constraints.Required(
      message = "A password is required.",
      groups = {InsertValidators.class, UpdateValidators.class}
  )
  private String password;
}
