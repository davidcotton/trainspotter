package controllers.api;

import static java.util.Objects.requireNonNull;
import static play.libs.Json.toJson;
import static utilities.JsonHelper.MESSAGE_NOT_FOUND;
import static utilities.JsonHelper.errorsAsJson;

import controllers.Security;
import javax.inject.Inject;
import models.create.CreateUser;
import models.LoginUser;
import models.update.UpdateUser;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import services.UserService;
import views.html.user.login;

public class UserController extends Controller {

  private final UserService userService;
  private final FormFactory formFactory;

  @Inject
  public UserController(UserService userService, FormFactory formFactory) {
    this.userService = requireNonNull(userService);
    this.formFactory = requireNonNull(formFactory);
  }

  /**
   * Fetch all active users.
   *
   * @return A collection of active users.
   */
  @Security.Authenticated
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
    return userService.findActiveById(id)
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
        .insert(formFactory.form(CreateUser.class).bindFromRequest())
        .fold(
            form -> badRequest(errorsAsJson(form.errors())),
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
        .findActiveById(id)
        .map(savedUser -> userService
            .updateUser(formFactory.form(UpdateUser.class).bindFromRequest(), savedUser)
            .fold(
                form -> badRequest(errorsAsJson(form.errors())),
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
    return userService
        .findActiveById(id)
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
    Form<LoginUser> loginForm = formFactory.form(LoginUser.class).bindFromRequest();
    if (loginForm.hasErrors()) {
      return badRequest(login.render(loginForm));
    }

    return userService
        .loginToken(loginForm.get())
        .fold(
            error -> badRequest(errorsAsJson(error)),
            token -> ok(toJson(token))
        );
  }
}
