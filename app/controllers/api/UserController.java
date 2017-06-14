package controllers.api;

import static java.util.Objects.requireNonNull;
import static play.libs.Json.fromJson;
import static play.libs.Json.toJson;
import static utilities.JsonHelper.MESSAGE_NOT_FOUND;
import static utilities.JsonHelper.errorsAsJson;

import controllers.Security;
import javax.inject.Inject;
import models.CreateUser;
import models.LoginUser;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import services.UserService;

public class UserController extends Controller {

  private final UserService userService;

  @Inject
  public UserController(UserService userService) {
    this.userService = requireNonNull(userService);
  }

  /**
   * Fetch all active users.
   *
   * @return A collection of active users.
   */
  public Result fetchAll() {
    return ok(toJson(userService.fetchAll()));
  }

  /**
   * Fetch a single user by their ID.
   *
   * @param id The users ID.
   * @return The user if found else a 404 Not Found.
   */
  public Result fetch(long id) {
    return userService.findById(id)
        .map(user -> ok(toJson(user)))
        .orElse(notFound(errorsAsJson(MESSAGE_NOT_FOUND)));
  }

  /**
   * Create a new user.
   *
   * @return The created user if successful else the errors.
   */
  @BodyParser.Of(BodyParser.Json.class)
  public Result create() {
    return userService
        .insert(fromJson(request().body().asJson(), CreateUser.class))
        .fold(
            error -> badRequest(errorsAsJson(error)),
            user -> created(toJson(user))
        );
  }

  /**
   * Update a user in the system.
   *
   * @param id The user's ID.
   * @return The updated user if successful else the errors.
   */
  @Security.Authenticated
  @BodyParser.Of(BodyParser.Json.class)
  public Result update(long id) {
    return userService
        .findById(id)
        .map(savedUser -> userService
            .update(savedUser, fromJson(request().body().asJson(), CreateUser.class))
            .fold(
                error -> badRequest(errorsAsJson(error)),
                newUser -> created(toJson(newUser))
            )
        )
        .orElse(notFound(errorsAsJson(MESSAGE_NOT_FOUND)));
  }

  /**
   * Delete a user in the system.
   *
   * Uses a soft-delete approach to mark a user as deleted.
   *
   * @param id The user's ID
   * @return 204 No Content if successful else the error.
   */
  @Security.Authenticated
  public Result delete(long id) {
    return userService.findById(id)
        .map(user -> {
          userService.delete(user);
          return (Result) noContent();
        })
        .orElse(notFound(errorsAsJson(MESSAGE_NOT_FOUND)));
  }

  /**
   * Login into the system.
   *
   * @return An authentication Token.
   */
  @BodyParser.Of(BodyParser.Json.class)
  public Result login() {
    return userService
        .login(fromJson(request().body().asJson(), LoginUser.class))
        .fold(
            error -> badRequest(errorsAsJson(error)),
            token -> ok(toJson(token))
        );
  }
}
