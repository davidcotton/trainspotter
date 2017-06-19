package controllers;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import javax.inject.Inject;
import models.CreateUser;
import models.LoginUser;
import models.UpdateUser;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import repositories.UserRepository;
import services.AuthenticationService;
import views.html.user.edit;
import views.html.user.index;
import views.html.user.login;
import views.html.user.register;
import views.html.user.view;
import views.html.notFound;

public class UserController extends Controller {

  private final UserRepository userRepository;
  private final FormFactory formFactory;
  private final AuthenticationService authenticationService;

  @Inject
  public UserController(
      UserRepository userRepository,
      FormFactory formFactory,
      AuthenticationService authenticationService
  ) {
    this.userRepository = requireNonNull(userRepository);
    this.formFactory = requireNonNull(formFactory);
    this.authenticationService = requireNonNull(authenticationService);
  }

  /**
   * View all users.
   *
   * @return A page with all users.
   */
  public Result index() {
    return ok(index.render(userRepository.findAll()));
  }

  /**
   * View a single user.
   *
   * @param id The user's ID.
   * @return A user page if found.
   */
  public Result view(long id) {
    return userRepository
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
    Form<CreateUser> userForm = formFactory
        .form(CreateUser.class, CreateUser.InsertValidators.class)
        .bindFromRequest();
    if (userForm.hasErrors()) {
      return badRequest(register.render(userForm));
    }

    CreateUser createUser = userForm.get();
    User user = new User(createUser);
    userRepository.insert(user);

    return Results.redirect(routes.ApplicationController.index());
  }

  /**
   * Display an edit user page.
   *
   * @param id The user's ID.
   * @return An edit user page if user is found else not found page.
   */
  public Result editForm(long id) {
    return userRepository.
        findById(id)
        .map(user -> ok(edit.render(
            id,
            formFactory.form(UpdateUser.class).fill(new UpdateUser(user)))
        ))
        .orElse(notFound(notFound.render()));
  }

  public Result editUserSubmit(long id) {
    Optional<User> maybeUser = userRepository.findById(id);
    if (!maybeUser.isPresent()) {
      return notFound(notFound.render());
    }

    Form<UpdateUser> userForm = formFactory.form(UpdateUser.class).bindFromRequest();
    if (userForm.hasErrors()) {
      return badRequest(edit.render(id, userForm));
    }

    User user = maybeUser.get();
    UpdateUser updateUser = userForm.get();

    // copy over new fields
    user.setEmail(updateUser.getEmail());
    user.setDisplayName(updateUser.getDisplayName());

    userRepository.update(user);

    return Results.redirect(routes.UserController.editForm(id));
  }

  public Result editPasswordSubmit(long id) {
    return TODO;
  }

  public Result delete(long id) {
    Optional<User> maybeUser = userRepository.findById(id);
    if (!maybeUser.isPresent()) {
      return notFound(notFound.render());
    }

    User user = maybeUser.get();
    user.setStatus(User.Status.deleted);
    userRepository.update(user);

    return ok(view.render(user));
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
    Form<LoginUser> loginForm = formFactory
        .form(LoginUser.class, LoginUser.Validators.class)
        .bindFromRequest();
    if (loginForm.hasErrors()) {
      return badRequest(login.render(loginForm));
    }

    LoginUser loginUser = loginForm.get();
    Optional<User> maybeUser = userRepository.findByEmail(loginUser.getEmail());
    if (!maybeUser.isPresent()) {
      return notFound(login.render(loginForm));
    }

    User user = maybeUser.get();

    boolean result = authenticationService.checkPassword(loginUser.getPassword(), user.getHash());
    if (result) {
      session().clear();
      session("email", user.getEmail());
      return Results.redirect(routes.ApplicationController.index());
    } else {
      return badRequest(login.render(loginForm));
    }
  }

  public Result logout() {
    session().clear();
    flash("success", "You've been logged out");
    return redirect(routes.ApplicationController.index());
  }
}
