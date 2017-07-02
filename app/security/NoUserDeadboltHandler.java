package security;

import java.util.List;
import java.util.concurrent.CompletionStage;

import be.objectify.deadbolt.java.AbstractDeadboltHandler;
import be.objectify.deadbolt.java.ConstraintPoint;
import be.objectify.deadbolt.java.ExecutionContextProvider;
import be.objectify.deadbolt.java.models.Permission;
import play.mvc.Http;

public class NoUserDeadboltHandler extends AbstractDeadboltHandler {

  public NoUserDeadboltHandler(ExecutionContextProvider ecProvider) {
    super(ecProvider);
  }

  /**
   * Gets the canonical name of the handler.  Defaults to the class name.
   *
   * @return whatever the implementor considers the canonical name of the handler to be
   */
  @Override
  public String handlerName() {
    return null;
  }

  /**
   * Invoked when access to a resource is authorized.
   *
   * @param context         the context, can be used to get various bits of information such as the
   *                        route and method
   * @param constraintType  the type of constraint, e.g. Dynamic, etc
   * @param constraintPoint the point at which the constraint was applied
   */
  @Override
  public void onAuthSuccess(Http.Context context, String constraintType, ConstraintPoint constraintPoint) {

  }

  /**
   * Get the permissions associated with a role.
   *
   * @param roleName the role the permissions are associated with
   * @return a non-null list containing the permissions associated with the role
   */
  @Override
  public CompletionStage<List<? extends Permission>> getPermissionsForRole(String roleName) {
    return null;
  }
}
