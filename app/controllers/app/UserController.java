package controllers.app;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import javax.inject.Inject;
import models.create.CreateUser;
import models.LoginUser;
import models.update.UpdatePassword;
import models.update.UpdateUser;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.Security;
import security.SessionAuthenticator;
import services.UserService;
import views.html.user.list;
import views.html.user.login;
import views.html.user.profile;
import views.html.user.register;
import views.html.user.settings;
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

  @Security.Authenticated(SessionAuthenticator.class)
  public Result list() {
    return ok(list.render(userService.fetchAll()));
  }

  public Result view(String slug) {
    return userService
        .findBySlug(slug)
        .map(user -> ok(view.render(user)))
        .orElse(notFound(notFound.render()));
  }

  public Result profile(String slug) {
    if (!slug.equals(session("userslug"))) {
      return Results.redirect(routes.ApplicationController.index());
    }

    return userService
        .findBySlug(slug)
        .map(user -> ok(profile.render(user)))
        .orElse(notFound(notFound.render()));
  }

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

  @Security.Authenticated(SessionAuthenticator.class)
  public Result settingsForm(String slug) {
    if (!slug.equals(session("userslug"))) {
      return Results.redirect(routes.ApplicationController.index());
    }

    return userService.
        findBySlug(slug)
        .map(
            user -> ok(settings.render(
              formFactory.form(UpdateUser.class).fill(new UpdateUser(user)),
              formFactory.form(UpdatePassword.class),
              user
          ))
        )
        .orElse(notFound(notFound.render()));
  }

  @Security.Authenticated(SessionAuthenticator.class)
  public Result editUserSubmit(String slug) {
    if (!slug.equals(session("userslug"))) {
      return Results.redirect(routes.ApplicationController.index());
    }

    return userService
        .findBySlug(slug)
        .map(savedUser -> userService
            .updateUser(formFactory.form(UpdateUser.class).bindFromRequest(), savedUser)
            .fold(
                userForm -> badRequest(settings.render(
                    userForm,
                    formFactory.form(UpdatePassword.class),
                    savedUser
                )),
                newUser -> {
                  setLoginSession(newUser);
                  return Results.redirect(routes.UserController.view(newUser.getUsername()));
                }
            )
        )
        .orElse(notFound(notFound.render()));
  }

  @Security.Authenticated(SessionAuthenticator.class)
  public Result editPasswordSubmit(String slug) {
    if (!slug.equals(session("userslug"))) {
      return Results.redirect(routes.ApplicationController.index());
    }

    return userService
        .findBySlug(slug)
        .map(savedUser -> userService
            .updatePassword(formFactory.form(UpdatePassword.class).bindFromRequest(), savedUser)
            .fold(
                passwordForm -> badRequest(settings.render(
                    formFactory.form(UpdateUser.class).fill(new UpdateUser(savedUser)),
                    passwordForm,
                    savedUser
                )),
                newUser -> {
                  setLoginSession(newUser);
                  return Results.redirect(routes.UserController.view(newUser.getUsername()));
                }
            )
        )
        .orElse(notFound(notFound.render()));
  }

  @Security.Authenticated(SessionAuthenticator.class)
  public Result delete(String slug) {
    if (!slug.equals(session("userslug"))) {
      return Results.redirect(routes.ApplicationController.index());
    }

    return userService
        .findBySlug(slug)
        .map(user -> {
          userService.delete(user);
          return Results.redirect(routes.ApplicationController.index());
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

    if (user.isAuthorised(loginUser.getPassword())) {
      setLoginSession(user);
      return Results.redirect(routes.ApplicationController.index());
    } else {
      return badRequest(login.render(loginForm));
    }
  }

  @Security.Authenticated(SessionAuthenticator.class)
  public Result logout() {
    session().clear();
    flash("success", "You've been logged out");
    return redirect(routes.ApplicationController.index());
  }

  private void setLoginSession(User user) {
    session().clear();
    session("username", user.getUsername());
    session("userslug", user.getSlug());
  }
}
