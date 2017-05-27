package validators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

public class CustomConstraints {

  @Target({ElementType.FIELD})
  @Retention(RetentionPolicy.RUNTIME)
  @Constraint(validatedBy = UniqueEmailValidator.class)
  public @interface UniqueEmail {

    /**
     * The error message shown when an email address is already present.
     * This is required by JR303.
     *
     * @return The error message.
     */
    String message() default UniqueEmailValidator.message;

    /**
     * Groups allow you to restrict the set of constraints applied during validation.
     * This is required by JR303.
     *
     * @return Constraint groups.
     */
    Class<?>[] groups() default {};

    /**
     * Payload type that can be attached to a given constraint declaration.
     * This is required by JR303.
     *
     * @return Constraint payload.
     */
    Class<? extends Payload>[] payload() default {};
  }
}
