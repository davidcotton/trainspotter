package controllers;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import services.UserService;
import views.html.user.update;
import views.html.user.view;

public class UserController extends Controller {

  private final UserService userService;
  private final FormFactory formFactory;

  @Inject
  public UserController(UserService userService, FormFactory formFactory) {
    this.userService = requireNonNull(userService);
    this.formFactory = requireNonNull(formFactory);
  }

  public Result index() {
    List<User> users = userService.fetchAll();
    return ok(views.html.user.index.render(users));
  }

  public Result view(long id) {
    Optional<User> maybeUser = userService.findById(id);
    return ok(view.render(maybeUser.get()));
  }

  public Result update(long id) {
    Form<User> userForm = formFactory.form(User.class).bindFromRequest();
    if (userForm.hasErrors()) {
      return badRequest(update.render(id, userForm));
    }

    User user = userForm.get();
//    userService.update(user);

    return ok(update.render(id, userForm));
  }

  public Result create() {
    return TODO;
//    return userService
//        .insert(fromJson(request().body().asJson(), User.class))
//        .fold(
//            error -> badRequest(errorsAsJson(error)),
//            user -> created(toJson(user))
//        );
  }

  public Result submit() {
    return TODO;
  }

  public Result delete(long id) {
    return TODO;
  }
}
