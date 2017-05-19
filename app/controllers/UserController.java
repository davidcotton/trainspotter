package controllers;

import static java.util.Objects.requireNonNull;
import static play.libs.Json.fromJson;
import static play.libs.Json.toJson;
import static utils.JsonHelper.errorsAsJson;

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
    return ok(toJson(userService.findAll()));
  }

  public Result find(long id) {
    return userService.findById(id)
        .map(user -> ok(toJson(user)))
        .orElse(notFound(toJson("Not found")));
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
