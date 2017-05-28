package models;

import lombok.AllArgsConstructor;
import lombok.Data;
import play.data.validation.Constraints;

@Data
@AllArgsConstructor
public class CreatePassword {

  /** Validator group to be called on insert. */
  public interface InsertValidators {}

  /** Validator group to be called on update. */
  public interface UpdateValidators {}

  @Constraints.Required(
      message = "A password is required.",
      groups = {InsertValidators.class, UpdateValidators.class}
  )
  private String password;
}
