package security;

import be.objectify.deadbolt.java.AbstractDeadboltHandler;
import be.objectify.deadbolt.java.ConstraintPoint;
import be.objectify.deadbolt.java.ExecutionContextProvider;
import be.objectify.deadbolt.java.models.Permission;
import be.objectify.deadbolt.java.models.Subject;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import models.User;
import play.mvc.Http.Context;
import play.mvc.Result;
import services.UserService;

public class SessionDeadboltHandler extends AbstractDeadboltHandler {

  private final UserService userService;

  public SessionDeadboltHandler(final ExecutionContextProvider ecProvider, UserService userService) {
    super(ecProvider);
    this.userService = userService;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CompletionStage<Optional<Result>> beforeAuthCheck(Context context) {
    return CompletableFuture.completedFuture(Optional.empty());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CompletionStage<Optional<? extends Subject>> getSubject(Context context) {
    String user = context.session().get("userslug");
    Optional<User> maybeUser = userService.findBySlug(user);
    return CompletableFuture.supplyAsync(() -> maybeUser, (Executor) executionContextProvider.get());
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
  public void onAuthSuccess(Context context, String constraintType, ConstraintPoint constraintPoint) {

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
