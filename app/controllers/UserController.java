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
import views.html.user.add;
import views.html.user.edit;
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

  public Result add() {
    Form<CreateUser> userForm = formFactory.form(CreateUser.class).bindFromRequest();
    return ok(add.render(userForm));
  }

  public Result edit(long id) {
    Optional<User> maybeUser = userRepository.findById(id);
    if (maybeUser.isPresent()) {
      Form<User> userForm = formFactory.form(User.class).fill(maybeUser.get());
      return ok(edit.render(id, userForm));
    } else {
      return notFound("User not found");
    }

//    if (userForm.hasErrors()) {
//      return badRequest(update.render(id, userForm));
//    }
//
//    User user = userForm.get();
//    userRepository.insert(user);
//
//    return ok(update.render(id, userForm));
  }

  public Result submit() {
    return TODO;

//    Form<CreateUser> userForm = formFactory.form(CreateUser.class).bindFromRequest();
//    if (userForm.hasErrors()) {
//      return badRequest(create.render(userForm));
//    } else {
//      CreateUser createUser = userForm.get();
//      User user = new User(createUser);
//      return redirect(routes.UserController.index());
//    }
  }

  public Result delete(long id) {
    return TODO;
  }
}
