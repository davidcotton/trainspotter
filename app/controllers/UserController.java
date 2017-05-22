package controllers;

import static java.util.Objects.requireNonNull;
import static play.libs.Json.fromJson;
import static play.libs.Json.toJson;
import static utils.JsonHelper.MESSAGE_NOT_FOUND;
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

  public Result fetchAll() {
    return ok(toJson(userService.findAll()));
  }

  public Result fetch(long id) {
    return userService.findById(id)
        .map(user -> ok(toJson(user)))
        .orElse(notFound(errorsAsJson(MESSAGE_NOT_FOUND)));
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
    return userService
        .findById(id)
        .map(savedUser -> userService
            .update(savedUser, fromJson(request().body().asJson(), User.class))
            .fold(
                error -> badRequest(errorsAsJson(error)),
                newUser -> created(toJson(newUser))
            )
        )
        .orElse(notFound(errorsAsJson(MESSAGE_NOT_FOUND)));
  }

  public Result delete(long id) {
    return TODO;
  }
}
