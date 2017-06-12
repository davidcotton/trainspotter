package controllers;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;
import javax.inject.Inject;

import models.CreateUser;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import repositories.UserRepository;
import services.UserService;
import views.html.user.create;
import views.html.user.update;
import views.html.user.view;

public class UserController extends Controller {

  private final UserRepository userRepository;
  private final FormFactory formFactory;

  @Inject
  public UserController(UserRepository userRepository, FormFactory formFactory) {
    this.userRepository = requireNonNull(userRepository);
    this.formFactory = requireNonNull(formFactory);
  }

  public Result index() {
    List<User> users = userRepository.findAll();
    return ok(views.html.user.index.render(users));
  }

  public Result view(long id) {
    Optional<User> maybeUser = userRepository.findById(id);
    return ok(view.render(maybeUser.get()));
  }

  public Result edit(long id) {
    Form<User> userForm = formFactory.form(User.class).bindFromRequest();
    if (userForm.hasErrors()) {
      return badRequest(update.render(id, userForm));
    }

    User user = userForm.get();
//    userService.edit(user);

    return ok(update.render(id, userForm));
  }

  public Result add() {
//    return TODO;

  }

  public Result submit() {
    Form<CreateUser> userForm = formFactory.form(CreateUser.class).bindFromRequest();
    if (userForm.hasErrors()) {
      return badRequest(create.render(userForm));
    } else {
      CreateUser createUser = userForm.get();
      User user = new User(createUser);
      return redirect(routes.UserController.index());
    }
  }

  public Result delete(long id) {
    return TODO;
  }
}
