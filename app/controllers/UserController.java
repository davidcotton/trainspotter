package controllers;

import static java.util.Objects.requireNonNull;
import static play.libs.Json.fromJson;
import static play.libs.Json.toJson;
import static utils.JsonHelper.errorsAsJson;

import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import models.User;
import play.mvc.Controller;
import play.mvc.Result;
import services.UserService;

public class UserController extends Controller {

  private final UserService userService;

  @Inject
  public UserController(UserService userService) {
    this.userService = requireNonNull(userService);
  }

  public Result findAll() {
    List<User> users = userService.findAll();
    return ok(views.html.user.index.render(users));
  }

  public Result find(long id) {
    Optional<User> maybeUser = userService.findById(id);
    if (maybeUser.isPresent()) {
      return ok(views.html.user.view.render(maybeUser.get()));
    } else {
      // @todo fix
      return ok(views.html.user.view.render(userService.findById(1).get()));
    }
  }

  public Result create() {
    return userService
        .insert(fromJson(request().body().asJson(), User.class))
        .fold(
            error -> badRequest(errorsAsJson(error)),
            user -> created(toJson(user))
        );
  }

  public Result update(long id) {
    return TODO;
  }

  public Result delete(long id) {
    return TODO;
  }
}
