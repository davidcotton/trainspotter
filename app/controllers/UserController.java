package controllers;

import static java.util.Objects.requireNonNull;
import static play.libs.Json.fromJson;
import static play.libs.Json.toJson;
import static utilities.JsonHelper.MESSAGE_NOT_FOUND;
import static utilities.JsonHelper.errorsAsJson;

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

  public Result fetchAll() {
    return ok(toJson(userService.fetchAll()));
  }

  public Result fetch(long id) {
    return userService.findById(id)
        .map(user -> ok(toJson(user)))
        .orElse(notFound(errorsAsJson(MESSAGE_NOT_FOUND)));
  }

  @BodyParser.Of(BodyParser.Json.class)
  public Result create() {
    return userService
        .insert(fromJson(request().body().asJson(), CreateUser.class))
        .fold(
            error -> badRequest(errorsAsJson(error)),
            user -> created(toJson(user))
        );
  }

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

  public Result delete(long id) {
    return userService.findById(id)
        .map(user -> {
          userService.delete(user);
          return (Result) noContent();
        })
        .orElse(notFound(errorsAsJson(MESSAGE_NOT_FOUND)));
  }

  @BodyParser.Of(BodyParser.Json.class)
  public Result login() {
    return userService
        .login(fromJson(request().body().asJson(), LoginUser.class))
        .fold(
            error -> badRequest(errorsAsJson(error)),
            user -> ok(toJson(user))
        );
  }
}
