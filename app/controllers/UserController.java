package controllers;

import static java.util.Objects.requireNonNull;
import static play.libs.Json.toJson;

import javax.inject.Inject;
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
    return TODO;
  }

  public Result update(long id) {
    return TODO;
  }

  public Result delete(long id) {
    return TODO;
  }
}
