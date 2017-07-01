package controllers.app;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import javax.inject.Inject;
import models.create.CreateUser;
import models.LoginUser;
import models.update.UpdateUser;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.Security;
import services.UserService;
import views.html.user.edit;
import views.html.user.index;
import views.html.user.login;
import views.html.user.register;
import views.html.user.view;
import views.html.notFound;

public class UserController extends Controller {

  private final UserService userService;
  private final FormFactory formFactory;

  @Inject
  public UserController(UserService userService, FormFactory formFactory) {
    this.userService = requireNonNull(userService);
    this.formFactory = requireNonNull(formFactory);
  }

  /**
   * View all users.
   *
   * @return A page with all users.
   */
  @Security.Authenticated(Secured.class)
  public Result index() {
    return ok(index.render(userService.fetchAll()));
  }

  /**
   * View a single user.
   *
   * @param id The user's ID.
   * @return A user page if found.
   */
  public Result view(long id) {
    return userService
        .findActiveById(id)
        .map(user -> ok(view.render(user)))
        .orElse(notFound(notFound.render()));
  }

  /**
   * Display the register form.
   *
   * @return A page to register.
   */
  public Result registerForm() {
    return ok(register.render(formFactory.form(CreateUser.class)));
  }

  public Result registerSubmit() {
    return userService
        .insert(formFactory.form(CreateUser.class).bindFromRequest())
        .fold(
            form -> badRequest(register.render(form)),
            user -> {
              setLoginSession(user);
              return Results.redirect(routes.ApplicationController.index());
            }
        );
  }

  /**
   * Display an edit user page.
   *
   * @param id The user's ID.
   * @return An edit user page if user is found else not found page.
   */
  @Security.Authenticated(Secured.class)
  public Result editForm(long id) {
    return userService.
        findActiveById(id)
        .map(user -> ok(edit.render(
            id,
            formFactory.form(UpdateUser.class).fill(new UpdateUser(user))
        )))
        .orElse(notFound(notFound.render()));
  }

  @Security.Authenticated(Secured.class)
  public Result editUserSubmit(long id) {
    return userService
        .findActiveById(id)
        .map(savedUser -> userService
            .update(formFactory.form(UpdateUser.class).bindFromRequest(), savedUser)
            .fold(
                userForm -> badRequest(edit.render(id, userForm)),
                newUser -> Results.redirect(routes.UserController.view(newUser.getId()))
            )
        )
        .orElse(notFound(notFound.render()));
  }

  @Security.Authenticated(Secured.class)
  public Result editPasswordSubmit(long id) {
    return TODO;
  }

  @Security.Authenticated(Secured.class)
  public Result delete(long id) {
    return userService
        .findActiveById(id)
        .map(user -> {
          userService.delete(user);
          return Results.redirect(routes.UserController.index());
        })
        .orElse(notFound(notFound.render()));
  }

  /**
   * Display a login form.
   *
   * @return The login form.
   */
  public Result loginForm() {
    return ok(login.render(formFactory.form(LoginUser.class)));
  }

  public Result loginSubmit() {
//    return userService.

    Form<LoginUser> loginForm = formFactory.form(LoginUser.class).bindFromRequest();
    if (loginForm.hasErrors()) {
      return badRequest(login.render(loginForm));
    }

    LoginUser loginUser = loginForm.get();
    Optional<User> maybeUser = userService.findByEmail(loginUser.getEmail());
    if (!maybeUser.isPresent()) {
      return notFound(login.render(loginForm));
    }

    User user = maybeUser.get();

    if (user.isValid(loginUser.getPassword())) {
      setLoginSession(user);
      return Results.redirect(routes.ApplicationController.index());
    } else {
      return badRequest(login.render(loginForm));
    }
  }

  @Security.Authenticated(Secured.class)
  public Result logout() {
    session().clear();
    flash("success", "You've been logged out");
    return redirect(routes.ApplicationController.index());
  }

  private void setLoginSession(User user) {
    session().clear();
    session("email", user.getEmail());
    session("user_id", String.valueOf(user.getId()));
    session("user_name", user.getDisplayName());
  }
}
